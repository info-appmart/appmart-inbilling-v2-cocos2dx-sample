#ifndef __SAMPLE_JNI_H__
#define __SAMPLE_JNI_H__

extern "C" {

extern void setInventory();
extern void requestPayment(int callService);
extern void requestConsume();

}

#endif // __SAMPLE_JNI_H__
