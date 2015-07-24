#include "SampleNativeBridge.h"
#include "SampleJni.h"

namespace sample {
void SampleNativeBridge::setInventoryBridge() {
	setInventory();
}
void SampleNativeBridge::requestPaymentBridge(int callService) {
	requestPayment(callService);
}
void SampleNativeBridge::requestConsumeBridge(){
	requestConsume();
}
}
