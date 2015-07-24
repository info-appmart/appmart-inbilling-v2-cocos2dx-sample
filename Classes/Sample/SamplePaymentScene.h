#ifndef __SAMPLE_PAYMENT_SCENE_H__
#define __SAMPLE_PAYMENT_SCENE_H__

#include "cocos2d.h"
class SamplePayment: public cocos2d::Layer {
public:
	
	static cocos2d::Scene* createScene();

	virtual bool init();

	void autoConsumePaymentService(cocos2d::Ref* pSender);
	void manualConsumePaymentService(cocos2d::Ref* pSender);
	void consumeStart(cocos2d::Ref* pSender);
	
	CREATE_FUNC(SamplePayment);
};

#endif // __SAMPLE_PAYMENT_SCENE_H__
