//
// Created by mahmoodms on 4/3/2017.
//

/*Additional Includes*/
#include <jni.h>
#include <android/log.h>
#include "punchDetection.h"

#define  LOG_TAG "jniExecutor-cpp"
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

//TODO

extern "C" {
JNIEXPORT jdoubleArray JNICALL
Java_com_yeolabgt_mahmoodms_ppg_DeviceControlActivity_jpunchDetection(JNIEnv *env, jobject jobject1, jdoubleArray accx, jdoubleArray accy, jdoubleArray accz, jdouble sampleRate) {
    jdouble *X1 = env->GetDoubleArrayElements(accx, nullptr);
    jdouble *X2 = env->GetDoubleArrayElements(accy, nullptr);
    jdouble *X3 = env->GetDoubleArrayElements(accz, nullptr);
    if (X1 == nullptr) LOGE("ERROR - C_ARRAY IS NULL");
    static double PeakAcc_data[2500];
    int PeakAcc_size[1];
    static double ImpactV_data[2500];
    int ImpactV_size[1];
    static double Force_data[2500];
    int Force_size[1];
    punchDetection(X1, X2, X3, sampleRate, PeakAcc_data, PeakAcc_size, ImpactV_data, ImpactV_size, Force_data, Force_size);
    int totalSize = PeakAcc_size[0] + ImpactV_size[0] + Force_size[0];
    // Allocate return data:
    double Y[totalSize];
    // Copy in data:
    memcpy(&Y[0], PeakAcc_data, sizeof(double) * PeakAcc_size[0]);
    memcpy(&Y[PeakAcc_size[0]], ImpactV_data, sizeof(double) * ImpactV_size[0]);
    memcpy(&Y[PeakAcc_size[0]+ImpactV_size[0]], Force_data, sizeof(double) * Force_size[0]);
    jdoubleArray m_result = env->NewDoubleArray(totalSize);
    env->SetDoubleArrayRegion(m_result, 0, totalSize, Y);
    return m_result;
}
}

extern "C" {
JNIEXPORT jint JNICALL
Java_com_yeolabgt_mahmoodms_ppg_DeviceControlActivity_jmainInitialization(
        JNIEnv *env, jobject obj, jboolean initialize) {
    if (!(bool) initialize) {
        punchDetection_initialize();
        return 0;
    } else {
        return -1;
    }
}
}
