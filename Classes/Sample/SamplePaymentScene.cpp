#include "SamplePaymentScene.h"
#include "SampleNativeBridge.h"

USING_NS_CC;

Scene* SamplePayment::createScene() {
	Scene* scene = Scene::create();
	SamplePayment* layer = SamplePayment::create();
	scene->addChild(layer);

	return scene;
}

bool SamplePayment::init() {
	if (!Layer::init()) {
		return false;
	}
	
	Size visibleSize = Director::getInstance()->getVisibleSize();
	Vec2 origin = Director::getInstance()->getVisibleOrigin();
	
	// ------------------------------ //
	// サービス設定
	// ------------------------------ //

	// 自動消費
	Sprite* autoConsumeButton1 = Sprite::create("AutoConsumeButton.png");
	Sprite* autoConsumeButton2 = Sprite::create("AutoConsumeButton.png");
	autoConsumeButton2->setColor(Color3B::BLUE);
	MenuItemSprite* autoConsumeButton = MenuItemSprite::create(
			autoConsumeButton1, autoConsumeButton2,
			CC_CALLBACK_1(SamplePayment::autoConsumePaymentService, this));
	autoConsumeButton->setPosition(
			Vec2(origin.x + visibleSize.width * 1 / 2,
					origin.y + visibleSize.height * 3 / 4));

	// 手動消費決済
	Sprite* manualConsumeButton1 = Sprite::create("ManualConsumeButton.png");
	Sprite* manualConsumeButton2 = Sprite::create("ManualConsumeButton.png");
	manualConsumeButton2->setColor(Color3B::BLUE);
	MenuItemSprite* manualConsumeButton = MenuItemSprite::create(manualConsumeButton1,
			manualConsumeButton2,
			CC_CALLBACK_1(SamplePayment::manualConsumePaymentService,this));
	manualConsumeButton->setPosition(
			Vec2(origin.x + visibleSize.width * 1 / 2,
					origin.y + visibleSize.height * 2 / 4));

	// 手動消費消化
	Sprite* consumeStartButton1 = Sprite::create("ConsumeStartButton.png");
	Sprite* consumeStartButton2 = Sprite::create("ConsumeStartButton.png");
	consumeStartButton2->setColor(Color3B::BLUE);
	MenuItemSprite* consumeStartButton = MenuItemSprite::create(consumeStartButton1,
			consumeStartButton2,
			CC_CALLBACK_1(SamplePayment::consumeStart, this));
	consumeStartButton->setPosition(
			Vec2(origin.x + visibleSize.width * 1 / 2,
					origin.y + visibleSize.height * 1 / 4));

	// ------------------------------ //
	// メニュー設定
	// ------------------------------ //
	Menu* menu = Menu::create(autoConsumeButton, manualConsumeButton,
			consumeStartButton, NULL);
	menu->setPosition(Vec2::ZERO);
	this->addChild(menu, 1);

	return true;
}

void SamplePayment::autoConsumePaymentService(cocos2d::Ref* pSender){
	int CALL_SERVICE = 1;
	sample::SampleNativeBridge::requestPaymentBridge(CALL_SERVICE);
}
void SamplePayment::manualConsumePaymentService(cocos2d::Ref* pSender){
	int CALL_SERVICE = 2;
	sample::SampleNativeBridge::requestPaymentBridge(CALL_SERVICE);
}
void SamplePayment::consumeStart(cocos2d::Ref* pSender){
	sample::SampleNativeBridge::requestConsumeBridge();
}
