//
//  Product.swift
//  SecondHand
//
//  Created by 에디 on 2023/06/19.
//

import Foundation

struct Product: Decodable {
    let productId: Int
    let title: String
    let town: Town
    let createdAt: String
    let isLiked: Bool
    let isMine: Bool
    let status: Int
    let price: Int
    let countInfo: CountInfo
    let imgUrl: String
}

extension Product {
    var isReserved: Bool {
        self.status == 0
    }
}
