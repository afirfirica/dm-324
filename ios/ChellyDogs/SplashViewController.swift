//
//  SplashViewController.swift
//  ChellyDogs
//
//  Created by 123 on 2/6/18.
//  Copyright © 2018 123. All rights reserved.
//

import UIKit

var counter = 3
var timer = Timer()

class SplashViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //Timer
        timer = Timer.scheduledTimer(timeInterval: 0.5, target: self, selector: #selector(timerAction), userInfo: nil, repeats: true)
        
        // Do any additional setup after loading the view.
    }
    
    @objc func timerAction() {
        counter -= 1
        if(counter == 0){
            timer.invalidate()
            performSegue(withIdentifier: "SplashSegue",
                         sender: self)
            
        }
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}
