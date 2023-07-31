//
//  AppCoordinator.swift
//  SecondHand
//
//  Created by 김하림 on 2023/07/18.
//

import UIKit

class AppCoordinator: NSObject, Coordinator {
    
    // MARK: Properties
    
    var delegate: CoordinatorFinishDelegate?
    
    let window: UIWindow
    
    var presenter: UINavigationController

    var childCoordinators: [Coordinator]
    
    var tabBarController: UITabBarController
    
    
    // MARK: Initializing
    
    init(window: UIWindow) {
        self.window = window
        
        let navigationController = UINavigationController()
        navigationController.setNavigationBarHidden(true, animated: true)
        self.presenter = navigationController
        
        self.childCoordinators = []
        self.tabBarController = UITabBarController()
    }
    
    func start() {
        window.rootViewController = presenter
        let coordinator = TabBarCoordinator(presenter: presenter)
        coordinator.delegate = self
        childCoordinators.append(coordinator)
        coordinator.start()
        
        window.makeKeyAndVisible()
    }
    
}