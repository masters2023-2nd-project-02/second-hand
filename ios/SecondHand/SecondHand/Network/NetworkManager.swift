//
//  NetworkManager.swift
//  SecondHand
//
//  Created by 에디 on 2023/06/06.
//

import Foundation
import UIKit.UIImage

class NetworkManager: NetworkManageable, URLRequestProducible {
    static let shared = NetworkManager()
    private let baseUrlString = Bundle.main.object(forInfoDictionaryKey: "baseUrl") as? String ?? ""
    private let dataDecoder: DataDecodable = DataDecoder()
    
    func fetchProducts(query: [String: String]) async -> [Product] {
        do {
            let urlRequest = try self.makeUrlRequest(baseUrlString, query: query, httpMethod: .get)
            let data = try await fetchData(with: urlRequest)
            let productsForm = try dataDecoder.decodeJSON(data, DTO: Form.self) as? Form
            let products: [Product]? = productsForm?.data
            return products ?? []
        } catch {
            print("error : \(error)")
            return []
        }
    }
    
    private func fetchData(with urlRequest: URLRequest) async throws -> Data {
        let (data, response) = try await URLSession.shared.data(for: urlRequest)
        guard let httpResponse = response as? HTTPURLResponse else {
            throw URLError(.badServerResponse)
        }
        
        let status = httpResponse.statusCode
        if status >= 200 && status < 300 {
            return data
        } else {
            throw NSError(domain: "com.SecondHand.fetchDataError", code: status, userInfo: nil)
        }
    }
    
    func loadImage(from urlString: String, completion: @escaping (UIImage?) -> Void) {
        do {
            let urlRequest = try self.makeUrlRequest(urlString, query: [:], httpMethod: .get)
            URLSession.shared.downloadTask(with: urlRequest) { (location, _, error) in
                if let error = error {
                    print("Error downloading image: \(error)")
                    completion(nil)
                    return
                }
                
                guard let location = location else {
                    print("Location is nil.")
                    completion(nil)
                    return
                }
                
                do {
                    let imageData = try Data(contentsOf: location)
                    let image = UIImage(data: imageData)
                    completion(image)
                } catch {
                    print("Error creating image from data: \(error)")
                    completion(nil)
                }
            }.resume()
        } catch {
            print("error : \(error)")
        }
    }
}

// GitHub OAuth
extension NetworkManager {
    func presentGithubOAuthLoginScreen() {
        let clientId: String = Bundle.main.object(forInfoDictionaryKey: "githubClientId") as? String ?? ""
        let urlStr = "https://github.com/login/oauth/authorize?client_id=\(clientId)"
        guard let url = URL(string: urlStr) else { return }
        UIApplication.shared.open(url)
    }
    
    func sendAuthorizationCode(_ code: String) async throws -> Decodable {
            let oauthLoginDirectory = Bundle.main.object(forInfoDictionaryKey: "OAuth Login URL Directory") as? String ?? ""
            let entireLoginUrl = self.baseUrlString + oauthLoginDirectory
            
            // TODO: iOS, FE 구분을 위한 헤더 작성 예정
        
        do {
            let urlRequest = try self.makeUrlRequest(entireLoginUrl, query: [:], header: [:], body: "", httpMethod: .post)
            let data = try await self.fetchData(with: urlRequest)
            let reponseData = try self.dataDecoder.decodeJSON(data, DTO: OAuthLoginResponseDTO.self)
            return reponseData
        } catch {
            print("error : \(error)")
            throw NetworkingError.failedToSendAuthorizationCode
        }
    }
}

protocol NetworkManageable {
    func fetchProducts(query: [String: String]) async -> [Product]
    func presentGithubOAuthLoginScreen()
}

enum NetworkingError: Error {
    case failedToSendAuthorizationCode
}
