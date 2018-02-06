//
//  PlaneDetector.h
//  Created  on 2018/2/05.
//  Copyright © 2018 . All rights reserved.
//

#import <Foundation/Foundation.h>
#import <SceneKit/SceneKit.h>

@interface PlaneDetector : NSObject

+ (SCNVector4)detectPlaneWithPoints:(NSArray *)points;

@end
