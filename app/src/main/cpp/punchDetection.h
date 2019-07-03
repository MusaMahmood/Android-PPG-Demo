//
// Academic License - for use in teaching, academic research, and meeting
// course requirements at degree granting institutions only.  Not for
// government, commercial, or other organizational use.
// File: punchDetection.h
//
// MATLAB Coder version            : 3.3
// C/C++ source code generated on  : 03-Jul-2019 17:25:30
//
#ifndef PUNCHDETECTION_H
#define PUNCHDETECTION_H

// Include Files
#include <cmath>
#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include "rt_nonfinite.h"
#include "rtwtypes.h"
#include "punchDetection_types.h"

// Function Declarations
extern void punchDetection(double accX[1250], double accY[1250], double accZ
  [1250], double Fs, double PeakAcc_data[], int PeakAcc_size[1], double
  ImpactV_data[], int ImpactV_size[1], double Force_data[], int Force_size[1]);
extern void punchDetection_initialize();
extern void punchDetection_terminate();

#endif

//
// File trailer for punchDetection.h
//
// [EOF]
//
