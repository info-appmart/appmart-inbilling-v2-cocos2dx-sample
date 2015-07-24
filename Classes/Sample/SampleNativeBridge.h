#ifndef __SAMPLE_NATIVE_BRIDGE_H__
#define __SAMPLE_NATIVE_BRIDGE_H__

namespace sample {
class SampleNativeBridge{
public:
	
	static void setInventoryBridge();
	static void requestPaymentBridge(int callService);
	static void requestConsumeBridge();
	
};
}

#endif // __SAMPLE_NATIVE_BRIDGE_H__
