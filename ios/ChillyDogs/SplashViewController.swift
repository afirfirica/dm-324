//
//  SplashViewController.swift
//  ChellyDogs
//
//  Created by 123 on 2/6/18.
//  Copyright © 2018 123. All rights reserved.
//

import UIKit



class SplashViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if defaults.object(forKey: "isFirst") == nil {
            
            defaults.set("No", forKey:"isFirst")
            defaults.synchronize()
            
            let storyboard = UIStoryboard(name: "Main", bundle: nil) //Write your storyboard name
            let viewController = storyboard.instantiateViewController(withIdentifier: "ViewController") as! ViewController
            self.navigationController?.pushViewController(viewController, animated: true)
        } else {
            self.navigationController?.popViewController(animated: true)
        }
        
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
