//
// Academic License - for use in teaching, academic research, and meeting
// course requirements at degree granting institutions only.  Not for
// government, commercial, or other organizational use.
// File: punchDetection.cpp
//
// MATLAB Coder version            : 3.3
// C/C++ source code generated on  : 03-Jul-2019 17:25:30
//

// Include Files
#include "rt_nonfinite.h"
#include "punchDetection.h"

// Type Definitions
#ifndef struct_emxArray_real_T_2500
#define struct_emxArray_real_T_2500

struct emxArray_real_T_2500
{
  double data[2500];
  int size[1];
};

#endif                                 //struct_emxArray_real_T_2500

// Function Declarations
static void assignFullOutputs(const double y[1250], const int iPk_data[], const
  int iPk_size[1], const double wxPk_data[], const int wxPk_size[2], const
  double bPk_data[], double YpkOut_data[], int YpkOut_size[1], double
  XpkOut_data[], int XpkOut_size[1], double WpkOut_data[], int WpkOut_size[1],
  double PpkOut_data[], int PpkOut_size[1]);
static void b_abs(const double x_data[], const int x_size[1], double y_data[],
                  int y_size[1]);
static void b_diff(const double x_data[], const int x_size[1], double y_data[],
                   int y_size[1]);
static void b_do_vectors(const int a_data[], const int a_size[1], const int
  b_data[], const int b_size[1], int c_data[], int c_size[1], int ia_data[], int
  ia_size[1], int ib_data[], int ib_size[1]);
static void b_flipud(int x_data[], int x_size[1]);
static void b_nullAssignment(double x_data[], int x_size[1], const boolean_T
  idx_data[], const int idx_size[1]);
static void b_sqrt(double x[1250]);
static void c_findPeaksSeparatedByMoreThanM(const int iPk_size[1], int idx_data[],
  int idx_size[1]);
static void c_removePeaksBelowMinPeakPromin(const double y[1250], int iPk_data[],
  int iPk_size[1], double pbPk_data[], int pbPk_size[1], int iLB_data[], int
  iLB_size[1], int iRB_data[], int iRB_size[1]);
static void combineFullPeaks(const double y[1250], const int iPk_data[], const
  int iPk_size[1], const double bPk_data[], const int bPk_size[1], const int
  iLBw_data[], const int iLBw_size[1], const int iRBw_data[], const int
  iRBw_size[1], const double wPk_data[], const int wPk_size[2], const int
  iInf_data[], const int iInf_size[1], int iPkOut_data[], int iPkOut_size[1],
  double bPkOut_data[], int bPkOut_size[1], double bxPkOut_data[], int
  bxPkOut_size[2], double byPkOut_data[], int byPkOut_size[2], double
  wxPkOut_data[], int wxPkOut_size[2]);
static void diff(const double x_data[], const int x_size[2], double y_data[],
                 int y_size[1]);
static void do_vectors(const int a_data[], const int a_size[1], const int
  b_data[], const int b_size[1], int c_data[], int c_size[1], int ia_data[], int
  ia_size[1], int ib_data[], int ib_size[1]);
static void fetchPeakExtents(const int idx_data[], const int idx_size[1], double
  bPk_data[], int bPk_size[1], double bxPk_data[], int bxPk_size[2], double
  byPk_data[], int byPk_size[2], double wxPk_data[], int wxPk_size[2]);
static void filter(const double b[2], const double x[1256], double zi, double y
                   [1256]);
static void filtfilt(const double x_in[1250], double y_out[1250]);
static void findExtents(const double y[1250], int iPk_data[], int iPk_size[1],
  const int iFin_data[], const int iFin_size[1], const int iInf_data[], const
  int iInf_size[1], const int iInflect_data[], const int iInflect_size[1],
  double bPk_data[], int bPk_size[1], double bxPk_data[], int bxPk_size[2],
  double byPk_data[], int byPk_size[2], double wxPk_data[], int wxPk_size[2]);
static void findLeftIntercept(const double y[1250], int *idx, int borderIdx,
  double refHeight);
static void findRightIntercept(const double y[1250], int *idx, int borderIdx,
  double refHeight);
static void findpeaks(const double Yin[1250], double Ypk_data[], int Ypk_size[1],
                      double Xpk_data[], int Xpk_size[1], double Wpk_data[], int
                      Wpk_size[1], double Ppk_data[], int Ppk_size[1]);
static void flipud(double x[1256]);
static void getAllPeaksCodegen(const double y[1250], int iPk_data[], int
  iPk_size[1], int iInf_data[], int iInf_size[1], int iInflect_data[], int
  iInflect_size[1]);
static void getHalfMaxBounds(const double y[1250], const int iPk_data[], const
  int iPk_size[1], const double base_data[], const int iLB_data[], const int
  iRB_data[], double bounds_data[], int bounds_size[2]);
static void getLeftBase(const double yTemp[1250], const int iPeak_data[], const
  int iPeak_size[1], const int iFinite_data[], const int iFinite_size[1], const
  int iInflect_data[], int iBase_data[], int iBase_size[1], int iSaddle_data[],
  int iSaddle_size[1]);
static void getPeakBase(const double yTemp[1250], const int iPk_data[], const
  int iPk_size[1], const int iFin_data[], const int iFin_size[1], const int
  iInflect_data[], const int iInflect_size[1], double peakBase_data[], int
  peakBase_size[1], int iLeftSaddle_data[], int iLeftSaddle_size[1], int
  iRightSaddle_data[], int iRightSaddle_size[1]);
static void getPeakWidth(const double y[1250], const int iPk_data[], const int
  iPk_size[1], const double pbPk_data[], const int pbPk_size[1], int iLB_data[],
  int iLB_size[1], int iRB_data[], int iRB_size[1], double wxPk_data[], int
  wxPk_size[2]);
static void keepAtMostNpPeaks(int idx_data[], int idx_size[1]);
static double linterp(double xa, double xb, double ya, double yb, double yc,
                      double bc);
static void nullAssignment(const double x_data[], const int x_size[1], const
  boolean_T idx_data[], const int idx_size[1], double b_x_data[], int b_x_size[1]);
static void power(const double a[1250], double y[1250]);
static void rdivide(const double x_data[], const int x_size[1], const double
                    y_data[], double z_data[], int z_size[1]);
static void removeSmallPeaks(const double y[1250], const int iFinite_data[],
  const int iFinite_size[1], int iPk_data[], int iPk_size[1]);
static double sum(const double x[16]);
static double trapz(const double x_data[], const int x_size[1]);

// Function Definitions

//
// Arguments    : const double y[1250]
//                const int iPk_data[]
//                const int iPk_size[1]
//                const double wxPk_data[]
//                const int wxPk_size[2]
//                const double bPk_data[]
//                double YpkOut_data[]
//                int YpkOut_size[1]
//                double XpkOut_data[]
//                int XpkOut_size[1]
//                double WpkOut_data[]
//                int WpkOut_size[1]
//                double PpkOut_data[]
//                int PpkOut_size[1]
// Return Type  : void
//
static void assignFullOutputs(const double y[1250], const int iPk_data[], const
  int iPk_size[1], const double wxPk_data[], const int wxPk_size[2], const
  double bPk_data[], double YpkOut_data[], int YpkOut_size[1], double
  XpkOut_data[], int XpkOut_size[1], double WpkOut_data[], int WpkOut_size[1],
  double PpkOut_data[], int PpkOut_size[1])
{
  int loop_ub;
  int i7;
  short tmp_data[2500];
  YpkOut_size[0] = iPk_size[0];
  loop_ub = iPk_size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    YpkOut_data[i7] = y[iPk_data[i7] - 1];
  }

  loop_ub = iPk_size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    tmp_data[i7] = (short)(1 + (short)(iPk_data[i7] - 1));
  }

  XpkOut_size[0] = iPk_size[0];
  loop_ub = iPk_size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    XpkOut_data[i7] = tmp_data[i7];
  }

  diff(wxPk_data, wxPk_size, WpkOut_data, WpkOut_size);
  PpkOut_size[0] = iPk_size[0];
  loop_ub = iPk_size[0];
  for (i7 = 0; i7 < loop_ub; i7++) {
    PpkOut_data[i7] = YpkOut_data[i7] - bPk_data[i7];
  }
}

//
// Arguments    : const double x_data[]
//                const int x_size[1]
//                double y_data[]
//                int y_size[1]
// Return Type  : void
//
static void b_abs(const double x_data[], const int x_size[1], double y_data[],
                  int y_size[1])
{
  int k;
  y_size[0] = (short)x_size[0];
  for (k = 0; k + 1 <= x_size[0]; k++) {
    y_data[k] = std::abs(x_data[k]);
  }
}

//
// Arguments    : const double x_data[]
//                const int x_size[1]
//                double y_data[]
//                int y_size[1]
// Return Type  : void
//
static void b_diff(const double x_data[], const int x_size[1], double y_data[],
                   int y_size[1])
{
  int ixLead;
  int iyLead;
  double work_data_idx_0;
  int m;
  double tmp1;
  if (x_size[0] == 0) {
    y_size[0] = 0;
  } else {
    ixLead = x_size[0] - 1;
    if (!(ixLead < 1)) {
      ixLead = 1;
    }

    if (ixLead < 1) {
      y_size[0] = 0;
    } else {
      y_size[0] = (short)(x_size[0] - 1);
      if (!((short)(x_size[0] - 1) == 0)) {
        ixLead = 1;
        iyLead = 0;
        work_data_idx_0 = x_data[0];
        for (m = 2; m <= x_size[0]; m++) {
          tmp1 = work_data_idx_0;
          work_data_idx_0 = x_data[ixLead];
          tmp1 = x_data[ixLead] - tmp1;
          ixLead++;
          y_data[iyLead] = tmp1;
          iyLead++;
        }
      }
    }
  }
}

//
// Arguments    : const int a_data[]
//                const int a_size[1]
//                const int b_data[]
//                const int b_size[1]
//                int c_data[]
//                int c_size[1]
//                int ia_data[]
//                int ia_size[1]
//                int ib_data[]
//                int ib_size[1]
// Return Type  : void
//
static void b_do_vectors(const int a_data[], const int a_size[1], const int
  b_data[], const int b_size[1], int c_data[], int c_size[1], int ia_data[], int
  ia_size[1], int ib_data[], int ib_size[1])
{
  int iafirst;
  int ncmax;
  int nc;
  int ialast;
  int ibfirst;
  int iblast;
  int b_ialast;
  int ak;
  int b_iblast;
  int b_ia_data[1250];
  int bk;
  iafirst = a_size[0];
  ncmax = b_size[0];
  if (iafirst < ncmax) {
    ncmax = iafirst;
  }

  c_size[0] = (short)ncmax;
  ia_size[0] = ncmax;
  ib_size[0] = ncmax;
  nc = 0;
  iafirst = 0;
  ialast = 1;
  ibfirst = 0;
  iblast = 1;
  while ((ialast <= a_size[0]) && (iblast <= b_size[0])) {
    b_ialast = ialast;
    ak = a_data[ialast - 1];
    while ((b_ialast < a_size[0]) && (a_data[b_ialast] == ak)) {
      b_ialast++;
    }

    ialast = b_ialast;
    b_iblast = iblast;
    bk = b_data[iblast - 1];
    while ((b_iblast < b_size[0]) && (b_data[b_iblast] == bk)) {
      b_iblast++;
    }

    iblast = b_iblast;
    if (ak == bk) {
      nc++;
      c_data[nc - 1] = ak;
      ia_data[nc - 1] = iafirst + 1;
      ib_data[nc - 1] = ibfirst + 1;
      ialast = b_ialast + 1;
      iafirst = b_ialast;
      iblast = b_iblast + 1;
      ibfirst = b_iblast;
    } else if (ak < bk) {
      ialast = b_ialast + 1;
      iafirst = b_ialast;
    } else {
      iblast = b_iblast + 1;
      ibfirst = b_iblast;
    }
  }

  if (ncmax > 0) {
    if (1 > nc) {
      iafirst = 0;
    } else {
      iafirst = nc;
    }

    for (ialast = 0; ialast < iafirst; ialast++) {
      b_ia_data[ialast] = ia_data[ialast];
    }

    ia_size[0] = iafirst;
    for (ialast = 0; ialast < iafirst; ialast++) {
      ia_data[ialast] = b_ia_data[ialast];
    }

    if (1 > nc) {
      iafirst = 0;
    } else {
      iafirst = nc;
    }

    for (ialast = 0; ialast < iafirst; ialast++) {
      b_ia_data[ialast] = ib_data[ialast];
    }

    ib_size[0] = iafirst;
    for (ialast = 0; ialast < iafirst; ialast++) {
      ib_data[ialast] = b_ia_data[ialast];
    }

    if (1 > nc) {
      iafirst = 0;
    } else {
      iafirst = nc;
    }

    for (ialast = 0; ialast < iafirst; ialast++) {
      b_ia_data[ialast] = c_data[ialast];
    }

    c_size[0] = iafirst;
    for (ialast = 0; ialast < iafirst; ialast++) {
      c_data[ialast] = b_ia_data[ialast];
    }
  }
}

//
// Arguments    : int x_data[]
//                int x_size[1]
// Return Type  : void
//
static void b_flipud(int x_data[], int x_size[1])
{
  int m;
  int md2;
  int i;
  int xtmp;
  m = x_size[0];
  md2 = x_size[0] >> 1;
  for (i = 1; i <= md2; i++) {
    xtmp = x_data[i - 1];
    x_data[i - 1] = x_data[m - i];
    x_data[m - i] = xtmp;
  }
}

//
// Arguments    : double x_data[]
//                int x_size[1]
//                const boolean_T idx_data[]
//                const int idx_size[1]
// Return Type  : void
//
static void b_nullAssignment(double x_data[], int x_size[1], const boolean_T
  idx_data[], const int idx_size[1])
{
  int nxin;
  int k0;
  int k;
  int nxout;
  double b_x_data[2500];
  nxin = x_size[0];
  k0 = 0;
  for (k = 1; k <= idx_size[0]; k++) {
    k0 += idx_data[k - 1];
  }

  nxout = x_size[0] - k0;
  k0 = -1;
  for (k = 1; k <= nxin; k++) {
    if ((k > idx_size[0]) || (!idx_data[k - 1])) {
      k0++;
      x_data[k0] = x_data[k - 1];
    }
  }

  if (1 > nxout) {
    k0 = 0;
  } else {
    k0 = nxout;
  }

  for (nxout = 0; nxout < k0; nxout++) {
    b_x_data[nxout] = x_data[nxout];
  }

  x_size[0] = k0;
  for (nxout = 0; nxout < k0; nxout++) {
    x_data[nxout] = b_x_data[nxout];
  }
}

//
// Arguments    : double x[1250]
// Return Type  : void
//
static void b_sqrt(double x[1250])
{
  int k;
  for (k = 0; k < 1250; k++) {
    x[k] = std::sqrt(x[k]);
  }
}

//
// Arguments    : const int iPk_size[1]
//                int idx_data[]
//                int idx_size[1]
// Return Type  : void
//
static void c_findPeaksSeparatedByMoreThanM(const int iPk_size[1], int idx_data[],
  int idx_size[1])
{
  int n;
  int y_data[2500];
  int yk;
  int k;
  if (iPk_size[0] < 1) {
    n = 0;
  } else {
    n = iPk_size[0];
  }

  if (n > 0) {
    y_data[0] = 1;
    yk = 1;
    for (k = 2; k <= n; k++) {
      yk++;
      y_data[k - 1] = yk;
    }
  }

  idx_size[0] = n;
  for (yk = 0; yk < n; yk++) {
    idx_data[yk] = y_data[yk];
  }
}

//
// Arguments    : const double y[1250]
//                int iPk_data[]
//                int iPk_size[1]
//                double pbPk_data[]
//                int pbPk_size[1]
//                int iLB_data[]
//                int iLB_size[1]
//                int iRB_data[]
//                int iRB_size[1]
// Return Type  : void
//
static void c_removePeaksBelowMinPeakPromin(const double y[1250], int iPk_data[],
  int iPk_size[1], double pbPk_data[], int pbPk_size[1], int iLB_data[], int
  iLB_size[1], int iRB_data[], int iRB_size[1])
{
  int x_size_idx_0;
  int idx;
  int ii;
  boolean_T x_data[1250];
  boolean_T exitg1;
  int ii_data[1250];
  int idx_data[1250];
  double b_pbPk_data[1250];
  x_size_idx_0 = iPk_size[0];
  idx = iPk_size[0];
  for (ii = 0; ii < idx; ii++) {
    x_data[ii] = (y[iPk_data[ii] - 1] - pbPk_data[ii] >= 140.0);
  }

  idx = 0;
  ii = 1;
  exitg1 = false;
  while ((!exitg1) && (ii <= x_size_idx_0)) {
    if (x_data[ii - 1]) {
      idx++;
      ii_data[idx - 1] = ii;
      if (idx >= x_size_idx_0) {
        exitg1 = true;
      } else {
        ii++;
      }
    } else {
      ii++;
    }
  }

  if (x_size_idx_0 == 1) {
    if (idx == 0) {
      x_size_idx_0 = 0;
    }
  } else if (1 > idx) {
    x_size_idx_0 = 0;
  } else {
    x_size_idx_0 = idx;
  }

  for (ii = 0; ii < x_size_idx_0; ii++) {
    idx_data[ii] = ii_data[ii];
  }

  for (ii = 0; ii < x_size_idx_0; ii++) {
    ii_data[ii] = iPk_data[idx_data[ii] - 1];
  }

  iPk_size[0] = x_size_idx_0;
  for (ii = 0; ii < x_size_idx_0; ii++) {
    iPk_data[ii] = ii_data[ii];
  }

  for (ii = 0; ii < x_size_idx_0; ii++) {
    b_pbPk_data[ii] = pbPk_data[idx_data[ii] - 1];
  }

  pbPk_size[0] = x_size_idx_0;
  for (ii = 0; ii < x_size_idx_0; ii++) {
    pbPk_data[ii] = b_pbPk_data[ii];
  }

  for (ii = 0; ii < x_size_idx_0; ii++) {
    ii_data[ii] = iLB_data[idx_data[ii] - 1];
  }

  iLB_size[0] = x_size_idx_0;
  for (ii = 0; ii < x_size_idx_0; ii++) {
    iLB_data[ii] = ii_data[ii];
  }

  for (ii = 0; ii < x_size_idx_0; ii++) {
    ii_data[ii] = iRB_data[idx_data[ii] - 1];
  }

  iRB_size[0] = x_size_idx_0;
  for (ii = 0; ii < x_size_idx_0; ii++) {
    iRB_data[ii] = ii_data[ii];
  }
}

//
// Arguments    : const double y[1250]
//                const int iPk_data[]
//                const int iPk_size[1]
//                const double bPk_data[]
//                const int bPk_size[1]
//                const int iLBw_data[]
//                const int iLBw_size[1]
//                const int iRBw_data[]
//                const int iRBw_size[1]
//                const double wPk_data[]
//                const int wPk_size[2]
//                const int iInf_data[]
//                const int iInf_size[1]
//                int iPkOut_data[]
//                int iPkOut_size[1]
//                double bPkOut_data[]
//                int bPkOut_size[1]
//                double bxPkOut_data[]
//                int bxPkOut_size[2]
//                double byPkOut_data[]
//                int byPkOut_size[2]
//                double wxPkOut_data[]
//                int wxPkOut_size[2]
// Return Type  : void
//
static void combineFullPeaks(const double y[1250], const int iPk_data[], const
  int iPk_size[1], const double bPk_data[], const int bPk_size[1], const int
  iLBw_data[], const int iLBw_size[1], const int iRBw_data[], const int
  iRBw_size[1], const double wPk_data[], const int wPk_size[2], const int
  iInf_data[], const int iInf_size[1], int iPkOut_data[], int iPkOut_size[1],
  double bPkOut_data[], int bPkOut_size[1], double bxPkOut_data[], int
  bxPkOut_size[2], double byPkOut_data[], int byPkOut_size[2], double
  wxPkOut_data[], int wxPkOut_size[2])
{
  int ia_data[1250];
  int ia_size[1];
  int iInfR_data[1250];
  int iInfR_size[1];
  int iInfL_data[1250];
  int iInfL_size[1];
  int loop_ub;
  int u0;
  int iFinite_data[1250];
  int iPkOut;
  int iInfinite_data[1250];
  short tmp_data[1250];
  int i6;
  do_vectors(iPk_data, iPk_size, iInf_data, iInf_size, iPkOut_data, iPkOut_size,
             ia_data, ia_size, iInfR_data, iInfR_size);
  b_do_vectors(iPkOut_data, iPkOut_size, iPk_data, iPk_size, iInfL_data,
               iInfL_size, ia_data, ia_size, iInfR_data, iInfR_size);
  loop_ub = ia_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    iFinite_data[u0] = ia_data[u0];
  }

  b_do_vectors(iPkOut_data, iPkOut_size, iInf_data, iInf_size, iInfL_data,
               iInfL_size, ia_data, ia_size, iInfR_data, iInfR_size);
  loop_ub = ia_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    iInfinite_data[u0] = ia_data[u0];
  }

  iPkOut = iPkOut_size[0];
  bPkOut_size[0] = (short)iPkOut_size[0];
  loop_ub = (short)iPkOut_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    bPkOut_data[u0] = 0.0;
  }

  loop_ub = bPk_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    bPkOut_data[iFinite_data[u0] - 1] = bPk_data[u0];
  }

  loop_ub = ia_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    ia_data[u0] = iInfinite_data[u0];
  }

  loop_ub = ia_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    bPkOut_data[ia_data[u0] - 1] = 0.0;
  }

  loop_ub = iInf_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    ia_data[u0] = iInf_data[u0] - 1;
  }

  for (loop_ub = 0; loop_ub + 1 <= (short)iInf_size[0]; loop_ub++) {
    if (ia_data[loop_ub] < 1) {
      iInfL_data[loop_ub] = 1;
    } else {
      iInfL_data[loop_ub] = ia_data[loop_ub];
    }
  }

  loop_ub = iInf_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    ia_data[u0] = iInf_data[u0] + 1;
  }

  for (loop_ub = 0; loop_ub + 1 <= (short)iInf_size[0]; loop_ub++) {
    u0 = ia_data[loop_ub];
    if (!(u0 < 1250)) {
      u0 = 1250;
    }

    iInfR_data[loop_ub] = u0;
  }

  bxPkOut_size[0] = iPkOut;
  bxPkOut_size[1] = 2;
  loop_ub = iPkOut << 1;
  for (u0 = 0; u0 < loop_ub; u0++) {
    bxPkOut_data[u0] = 0.0;
  }

  loop_ub = iLBw_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    tmp_data[u0] = (short)(1 + (short)(iLBw_data[u0] - 1));
  }

  loop_ub = iLBw_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    bxPkOut_data[iFinite_data[u0] - 1] = tmp_data[u0];
  }

  loop_ub = iRBw_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    tmp_data[u0] = (short)(1 + (short)(iRBw_data[u0] - 1));
  }

  loop_ub = iRBw_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    bxPkOut_data[(iFinite_data[u0] + iPkOut) - 1] = tmp_data[u0];
  }

  loop_ub = iInf_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    bxPkOut_data[iInfinite_data[u0] - 1] = 0.5 * (double)(short)((short)((short)
      (iInf_data[u0] - 1) + (short)(iInfL_data[u0] - 1)) + 2);
  }

  loop_ub = iInf_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    bxPkOut_data[(iInfinite_data[u0] + iPkOut) - 1] = 0.5 * (double)(short)
      ((short)((short)(iInf_data[u0] - 1) + (short)(iInfR_data[u0] - 1)) + 2);
  }

  byPkOut_size[0] = iPkOut;
  byPkOut_size[1] = 2;
  loop_ub = iPkOut << 1;
  for (u0 = 0; u0 < loop_ub; u0++) {
    byPkOut_data[u0] = 0.0;
  }

  loop_ub = iLBw_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    byPkOut_data[iFinite_data[u0] - 1] = y[iLBw_data[u0] - 1];
  }

  loop_ub = iRBw_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    byPkOut_data[(iFinite_data[u0] + iPkOut) - 1] = y[iRBw_data[u0] - 1];
  }

  loop_ub = (short)iInf_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    byPkOut_data[iInfinite_data[u0] - 1] = y[iInfL_data[u0] - 1];
  }

  loop_ub = (short)iInf_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    byPkOut_data[(iInfinite_data[u0] + iPkOut) - 1] = y[iInfR_data[u0] - 1];
  }

  wxPkOut_size[0] = iPkOut;
  wxPkOut_size[1] = 2;
  loop_ub = iPkOut << 1;
  for (u0 = 0; u0 < loop_ub; u0++) {
    wxPkOut_data[u0] = 0.0;
  }

  loop_ub = wPk_size[0];
  for (u0 = 0; u0 < 2; u0++) {
    for (i6 = 0; i6 < loop_ub; i6++) {
      wxPkOut_data[(iFinite_data[i6] + iPkOut * u0) - 1] = wPk_data[i6 +
        wPk_size[0] * u0];
    }
  }

  loop_ub = iInf_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    wxPkOut_data[iInfinite_data[u0] - 1] = 0.5 * (double)(short)((short)((short)
      (iInf_data[u0] - 1) + (short)(iInfL_data[u0] - 1)) + 2);
  }

  loop_ub = iInf_size[0];
  for (u0 = 0; u0 < loop_ub; u0++) {
    wxPkOut_data[(iInfinite_data[u0] + iPkOut) - 1] = 0.5 * (double)(short)
      ((short)((short)(iInf_data[u0] - 1) + (short)(iInfR_data[u0] - 1)) + 2);
  }
}

//
// Arguments    : const double x_data[]
//                const int x_size[2]
//                double y_data[]
//                int y_size[1]
// Return Type  : void
//
static void diff(const double x_data[], const int x_size[2], double y_data[],
                 int y_size[1])
{
  int stride;
  int ix;
  int iy;
  int s;
  y_size[0] = (short)x_size[0];
  if (!((short)x_size[0] == 0)) {
    stride = x_size[0];
    ix = 0;
    iy = 0;
    for (s = 1; s <= stride; s++) {
      y_data[iy] = x_data[ix + stride] - x_data[ix];
      ix++;
      iy++;
    }
  }
}

//
// Arguments    : const int a_data[]
//                const int a_size[1]
//                const int b_data[]
//                const int b_size[1]
//                int c_data[]
//                int c_size[1]
//                int ia_data[]
//                int ia_size[1]
//                int ib_data[]
//                int ib_size[1]
// Return Type  : void
//
static void do_vectors(const int a_data[], const int a_size[1], const int
  b_data[], const int b_size[1], int c_data[], int c_size[1], int ia_data[], int
  ia_size[1], int ib_data[], int ib_size[1])
{
  int na;
  int nb;
  int ncmax;
  int nc;
  int nia;
  int nib;
  int iafirst;
  int ialast;
  int ibfirst;
  int iblast;
  int b_ialast;
  int ak;
  int b_iblast;
  int bk;
  int b_ia_data[1250];
  short b_ib_data[1250];
  int b_c_data[2500];
  na = a_size[0];
  nb = b_size[0];
  ncmax = a_size[0] + b_size[0];
  c_size[0] = (short)ncmax;
  ia_size[0] = a_size[0];
  ib_size[0] = b_size[0];
  nc = -1;
  nia = -1;
  nib = 0;
  iafirst = 1;
  ialast = 1;
  ibfirst = 0;
  iblast = 1;
  while ((ialast <= na) && (iblast <= nb)) {
    b_ialast = ialast;
    ak = a_data[ialast - 1];
    while ((b_ialast < a_size[0]) && (a_data[b_ialast] == ak)) {
      b_ialast++;
    }

    ialast = b_ialast;
    b_iblast = iblast;
    bk = b_data[iblast - 1];
    while ((b_iblast < b_size[0]) && (b_data[b_iblast] == bk)) {
      b_iblast++;
    }

    iblast = b_iblast;
    if (ak == bk) {
      nc++;
      c_data[nc] = ak;
      nia++;
      ia_data[nia] = iafirst;
      ialast = b_ialast + 1;
      iafirst = b_ialast + 1;
      iblast = b_iblast + 1;
      ibfirst = b_iblast;
    } else if (ak < bk) {
      nc++;
      nia++;
      c_data[nc] = ak;
      ia_data[nia] = iafirst;
      ialast = b_ialast + 1;
      iafirst = b_ialast + 1;
    } else {
      nc++;
      nib++;
      c_data[nc] = bk;
      ib_data[nib - 1] = ibfirst + 1;
      iblast = b_iblast + 1;
      ibfirst = b_iblast;
    }
  }

  while (ialast <= na) {
    b_ialast = ialast;
    while ((b_ialast < a_size[0]) && (a_data[b_ialast] == a_data[ialast - 1])) {
      b_ialast++;
    }

    nc++;
    nia++;
    c_data[nc] = a_data[ialast - 1];
    ia_data[nia] = ialast;
    ialast = b_ialast + 1;
  }

  while (iblast <= nb) {
    b_iblast = iblast;
    while ((b_iblast < b_size[0]) && (b_data[b_iblast] == b_data[iblast - 1])) {
      b_iblast++;
    }

    nc++;
    nib++;
    c_data[nc] = b_data[iblast - 1];
    ib_data[nib - 1] = iblast;
    iblast = b_iblast + 1;
  }

  if (a_size[0] > 0) {
    if (1 > nia + 1) {
      nb = -1;
    } else {
      nb = nia;
    }

    for (nia = 0; nia <= nb; nia++) {
      b_ia_data[nia] = ia_data[nia];
    }

    ia_size[0] = nb + 1;
    nb++;
    for (nia = 0; nia < nb; nia++) {
      ia_data[nia] = b_ia_data[nia];
    }
  }

  if (b_size[0] > 0) {
    if (1 > nib) {
      nb = 0;
    } else {
      nb = nib;
    }

    na = b_size[0];
    for (nia = 0; nia < na; nia++) {
      b_ib_data[nia] = (short)ib_data[nia];
    }

    ib_size[0] = nb;
    for (nia = 0; nia < nb; nia++) {
      ib_data[nia] = b_ib_data[nia];
    }
  }

  if (ncmax > 0) {
    if (1 > nc + 1) {
      nb = -1;
    } else {
      nb = nc;
    }

    for (nia = 0; nia <= nb; nia++) {
      b_c_data[nia] = c_data[nia];
    }

    c_size[0] = nb + 1;
    nb++;
    for (nia = 0; nia < nb; nia++) {
      c_data[nia] = b_c_data[nia];
    }
  }
}

//
// Arguments    : const int idx_data[]
//                const int idx_size[1]
//                double bPk_data[]
//                int bPk_size[1]
//                double bxPk_data[]
//                int bxPk_size[2]
//                double byPk_data[]
//                int byPk_size[2]
//                double wxPk_data[]
//                int wxPk_size[2]
// Return Type  : void
//
static void fetchPeakExtents(const int idx_data[], const int idx_size[1], double
  bPk_data[], int bPk_size[1], double bxPk_data[], int bxPk_size[2], double
  byPk_data[], int byPk_size[2], double wxPk_data[], int wxPk_size[2])
{
  int loop_ub;
  int i13;
  double b_bPk_data[2500];
  int bxPk_size_idx_0;
  int i14;
  int byPk_size_idx_0;
  static double b_bxPk_data[5000];
  int b_loop_ub;
  double b_byPk_data[5000];
  loop_ub = idx_size[0];
  for (i13 = 0; i13 < loop_ub; i13++) {
    b_bPk_data[i13] = bPk_data[idx_data[i13] - 1];
  }

  bPk_size[0] = idx_size[0];
  loop_ub = idx_size[0];
  for (i13 = 0; i13 < loop_ub; i13++) {
    bPk_data[i13] = b_bPk_data[i13];
  }

  bxPk_size_idx_0 = idx_size[0];
  loop_ub = idx_size[0];
  for (i13 = 0; i13 < 2; i13++) {
    for (i14 = 0; i14 < loop_ub; i14++) {
      b_bxPk_data[i14 + bxPk_size_idx_0 * i13] = bxPk_data[(idx_data[i14] +
        bxPk_size[0] * i13) - 1];
    }
  }

  bxPk_size[0] = idx_size[0];
  bxPk_size[1] = 2;
  byPk_size_idx_0 = idx_size[0];
  loop_ub = idx_size[0];
  b_loop_ub = idx_size[0];
  for (i13 = 0; i13 < 2; i13++) {
    for (i14 = 0; i14 < loop_ub; i14++) {
      bxPk_data[i14 + bxPk_size[0] * i13] = b_bxPk_data[i14 + bxPk_size_idx_0 *
        i13];
    }

    for (i14 = 0; i14 < b_loop_ub; i14++) {
      b_byPk_data[i14 + byPk_size_idx_0 * i13] = byPk_data[(idx_data[i14] +
        byPk_size[0] * i13) - 1];
    }
  }

  byPk_size[0] = idx_size[0];
  byPk_size[1] = 2;
  bxPk_size_idx_0 = idx_size[0];
  loop_ub = idx_size[0];
  b_loop_ub = idx_size[0];
  for (i13 = 0; i13 < 2; i13++) {
    for (i14 = 0; i14 < loop_ub; i14++) {
      byPk_data[i14 + byPk_size[0] * i13] = b_byPk_data[i14 + byPk_size_idx_0 *
        i13];
    }

    for (i14 = 0; i14 < b_loop_ub; i14++) {
      b_bxPk_data[i14 + bxPk_size_idx_0 * i13] = wxPk_data[(idx_data[i14] +
        wxPk_size[0] * i13) - 1];
    }
  }

  wxPk_size[0] = idx_size[0];
  wxPk_size[1] = 2;
  loop_ub = idx_size[0];
  for (i13 = 0; i13 < 2; i13++) {
    for (i14 = 0; i14 < loop_ub; i14++) {
      wxPk_data[i14 + wxPk_size[0] * i13] = b_bxPk_data[i14 + bxPk_size_idx_0 *
        i13];
    }
  }
}

//
// Arguments    : const double b[2]
//                const double x[1256]
//                double zi
//                double y[1256]
// Return Type  : void
//
static void filter(const double b[2], const double x[1256], double zi, double y
                   [1256])
{
  int k;
  int naxpy;
  int j;
  double as;
  y[0] = zi;
  memset(&y[1], 0, 1255U * sizeof(double));
  for (k = 0; k < 1256; k++) {
    if (1256 - k < 2) {
      naxpy = 1;
    } else {
      naxpy = 2;
    }

    for (j = 0; j + 1 <= naxpy; j++) {
      y[k + j] += x[k] * b[j];
    }

    naxpy = !(1255 - k < 1);
    as = -y[k];
    j = 1;
    while (j <= naxpy) {
      y[k + 1] += as * -0.996863331833438;
      j = 2;
    }
  }
}

//
// Arguments    : const double x_in[1250]
//                double y_out[1250]
// Return Type  : void
//
static void filtfilt(const double x_in[1250], double y_out[1250])
{
  double d1;
  double d2;
  int i2;
  double y[1256];
  double b_y[1256];
  double dv0[2];
  d1 = 2.0 * x_in[0];
  d2 = 2.0 * x_in[1249];
  for (i2 = 0; i2 < 3; i2++) {
    y[i2] = d1 - x_in[3 - i2];
  }

  memcpy(&y[3], &x_in[0], 1250U * sizeof(double));
  for (i2 = 0; i2 < 3; i2++) {
    y[i2 + 1253] = d2 - x_in[1248 - i2];
  }

  for (i2 = 0; i2 < 2; i2++) {
    dv0[i2] = 0.998431665916719 + -1.9968633318334379 * (double)i2;
  }

  memcpy(&b_y[0], &y[0], 1256U * sizeof(double));
  filter(dv0, b_y, -0.99843166591671 * y[0], y);
  flipud(y);
  for (i2 = 0; i2 < 2; i2++) {
    dv0[i2] = 0.998431665916719 + -1.9968633318334379 * (double)i2;
  }

  memcpy(&b_y[0], &y[0], 1256U * sizeof(double));
  filter(dv0, b_y, -0.99843166591671 * y[0], y);
  flipud(y);
  memcpy(&y_out[0], &y[3], 1250U * sizeof(double));
}

//
// Arguments    : const double y[1250]
//                int iPk_data[]
//                int iPk_size[1]
//                const int iFin_data[]
//                const int iFin_size[1]
//                const int iInf_data[]
//                const int iInf_size[1]
//                const int iInflect_data[]
//                const int iInflect_size[1]
//                double bPk_data[]
//                int bPk_size[1]
//                double bxPk_data[]
//                int bxPk_size[2]
//                double byPk_data[]
//                int byPk_size[2]
//                double wxPk_data[]
//                int wxPk_size[2]
// Return Type  : void
//
static void findExtents(const double y[1250], int iPk_data[], int iPk_size[1],
  const int iFin_data[], const int iFin_size[1], const int iInf_data[], const
  int iInf_size[1], const int iInflect_data[], const int iInflect_size[1],
  double bPk_data[], int bPk_size[1], double bxPk_data[], int bxPk_size[2],
  double byPk_data[], int byPk_size[2], double wxPk_data[], int wxPk_size[2])
{
  double yFinite[1250];
  int loop_ub;
  int i10;
  double b_bPk_data[1250];
  int b_bPk_size[1];
  int iLB_data[1250];
  int iLB_size[1];
  int iRB_data[1250];
  int iRB_size[1];
  int b_iPk_size[1];
  int b_iPk_data[1250];
  static double b_wxPk_data[2500];
  int b_wxPk_size[2];
  memcpy(&yFinite[0], &y[0], 1250U * sizeof(double));
  loop_ub = iInf_size[0];
  for (i10 = 0; i10 < loop_ub; i10++) {
    yFinite[iInf_data[i10] - 1] = rtNaN;
  }

  getPeakBase(yFinite, iPk_data, iPk_size, iFin_data, iFin_size, iInflect_data,
              iInflect_size, b_bPk_data, b_bPk_size, iLB_data, iLB_size,
              iRB_data, iRB_size);
  b_iPk_size[0] = iPk_size[0];
  loop_ub = iPk_size[0];
  for (i10 = 0; i10 < loop_ub; i10++) {
    b_iPk_data[i10] = iPk_data[i10];
  }

  c_removePeaksBelowMinPeakPromin(yFinite, b_iPk_data, b_iPk_size, b_bPk_data,
    b_bPk_size, iLB_data, iLB_size, iRB_data, iRB_size);
  getPeakWidth(yFinite, b_iPk_data, b_iPk_size, b_bPk_data, b_bPk_size, iLB_data,
               iLB_size, iRB_data, iRB_size, b_wxPk_data, b_wxPk_size);
  combineFullPeaks(y, b_iPk_data, b_iPk_size, b_bPk_data, b_bPk_size, iLB_data,
                   iLB_size, iRB_data, iRB_size, b_wxPk_data, b_wxPk_size,
                   iInf_data, iInf_size, iPk_data, iPk_size, bPk_data, bPk_size,
                   bxPk_data, bxPk_size, byPk_data, byPk_size, wxPk_data,
                   wxPk_size);
}

//
// Arguments    : const double y[1250]
//                int *idx
//                int borderIdx
//                double refHeight
// Return Type  : void
//
static void findLeftIntercept(const double y[1250], int *idx, int borderIdx,
  double refHeight)
{
  while ((*idx >= borderIdx) && (y[*idx - 1] > refHeight)) {
    (*idx)--;
  }
}

//
// Arguments    : const double y[1250]
//                int *idx
//                int borderIdx
//                double refHeight
// Return Type  : void
//
static void findRightIntercept(const double y[1250], int *idx, int borderIdx,
  double refHeight)
{
  while ((*idx <= borderIdx) && (y[*idx - 1] > refHeight)) {
    (*idx)++;
  }
}

//
// Arguments    : const double Yin[1250]
//                double Ypk_data[]
//                int Ypk_size[1]
//                double Xpk_data[]
//                int Xpk_size[1]
//                double Wpk_data[]
//                int Wpk_size[1]
//                double Ppk_data[]
//                int Ppk_size[1]
// Return Type  : void
//
static void findpeaks(const double Yin[1250], double Ypk_data[], int Ypk_size[1],
                      double Xpk_data[], int Xpk_size[1], double Wpk_data[], int
                      Wpk_size[1], double Ppk_data[], int Ppk_size[1])
{
  int iFinite_data[1250];
  int iFinite_size[1];
  int iInfinite_data[1250];
  int iInfinite_size[1];
  int iInflect_data[1250];
  int iInflect_size[1];
  int tmp_data[1250];
  int tmp_size[1];
  int iPk_size[1];
  int loop_ub;
  int i3;
  int iPk_data[2500];
  static double bPk_data[2500];
  int bPk_size[1];
  static double bxPk_data[5000];
  int bxPk_size[2];
  static double byPk_data[5000];
  int byPk_size[2];
  static double wxPk_data[5000];
  int wxPk_size[2];
  int idx_data[2500];
  int b_iPk_size[1];
  int b_iPk_data[2500];
  getAllPeaksCodegen(Yin, iFinite_data, iFinite_size, iInfinite_data,
                     iInfinite_size, iInflect_data, iInflect_size);
  removeSmallPeaks(Yin, iFinite_data, iFinite_size, tmp_data, tmp_size);
  iPk_size[0] = tmp_size[0];
  loop_ub = tmp_size[0];
  for (i3 = 0; i3 < loop_ub; i3++) {
    iPk_data[i3] = tmp_data[i3];
  }

  findExtents(Yin, iPk_data, iPk_size, iFinite_data, iFinite_size,
              iInfinite_data, iInfinite_size, iInflect_data, iInflect_size,
              bPk_data, bPk_size, bxPk_data, bxPk_size, byPk_data, byPk_size,
              wxPk_data, wxPk_size);
  c_findPeaksSeparatedByMoreThanM(iPk_size, idx_data, tmp_size);
  keepAtMostNpPeaks(idx_data, tmp_size);
  fetchPeakExtents(idx_data, tmp_size, bPk_data, bPk_size, bxPk_data, bxPk_size,
                   byPk_data, byPk_size, wxPk_data, wxPk_size);
  b_iPk_size[0] = tmp_size[0];
  loop_ub = tmp_size[0];
  for (i3 = 0; i3 < loop_ub; i3++) {
    b_iPk_data[i3] = iPk_data[idx_data[i3] - 1];
  }

  assignFullOutputs(Yin, b_iPk_data, b_iPk_size, wxPk_data, wxPk_size, bPk_data,
                    Ypk_data, Ypk_size, Xpk_data, Xpk_size, Wpk_data, Wpk_size,
                    Ppk_data, Ppk_size);
}

//
// Arguments    : double x[1256]
// Return Type  : void
//
static void flipud(double x[1256])
{
  int i;
  double xtmp;
  for (i = 0; i < 628; i++) {
    xtmp = x[i];
    x[i] = x[1255 - i];
    x[1255 - i] = xtmp;
  }
}

//
// Arguments    : const double y[1250]
//                int iPk_data[]
//                int iPk_size[1]
//                int iInf_data[]
//                int iInf_size[1]
//                int iInflect_data[]
//                int iInflect_size[1]
// Return Type  : void
//
static void getAllPeaksCodegen(const double y[1250], int iPk_data[], int
  iPk_size[1], int iInf_data[], int iInf_size[1], int iInflect_data[], int
  iInflect_size[1])
{
  int nPk;
  int nInf;
  int nInflect;
  char dir;
  int kfirst;
  double ykfirst;
  boolean_T isinfykfirst;
  int k;
  double yk;
  boolean_T isinfyk;
  char previousdir;
  int i4;
  nPk = 0;
  nInf = 0;
  nInflect = -1;
  dir = 'n';
  kfirst = 0;
  ykfirst = rtInf;
  isinfykfirst = true;
  for (k = 0; k < 1250; k++) {
    yk = y[k];
    if (rtIsNaN(y[k])) {
      yk = rtInf;
      isinfyk = true;
    } else if (rtIsInf(y[k]) && (y[k] > 0.0)) {
      isinfyk = true;
      nInf++;
      iInf_data[nInf - 1] = k + 1;
    } else {
      isinfyk = false;
    }

    if (yk != ykfirst) {
      previousdir = dir;
      if (isinfyk || isinfykfirst) {
        dir = 'n';
        if (kfirst >= 1) {
          nInflect++;
          iInflect_data[nInflect] = kfirst;
        }
      } else if (yk < ykfirst) {
        dir = 'd';
        if ('d' != previousdir) {
          nInflect++;
          iInflect_data[nInflect] = kfirst;
          if (previousdir == 'i') {
            nPk++;
            iPk_data[nPk - 1] = kfirst;
          }
        }
      } else {
        dir = 'i';
        if ('i' != previousdir) {
          nInflect++;
          iInflect_data[nInflect] = kfirst;
        }
      }

      ykfirst = yk;
      kfirst = k + 1;
      isinfykfirst = isinfyk;
    }
  }

  if ((!isinfykfirst) && ((nInflect + 1 == 0) || (iInflect_data[nInflect] < 1250)))
  {
    nInflect++;
    iInflect_data[nInflect] = 1250;
  }

  if (1 > nPk) {
    iPk_size[0] = 0;
  } else {
    iPk_size[0] = nPk;
  }

  if (1 > nInf) {
    iInf_size[0] = 0;
  } else {
    iInf_size[0] = nInf;
  }

  if (1 > nInflect + 1) {
    i4 = -1;
  } else {
    i4 = nInflect;
  }

  iInflect_size[0] = i4 + 1;
}

//
// Arguments    : const double y[1250]
//                const int iPk_data[]
//                const int iPk_size[1]
//                const double base_data[]
//                const int iLB_data[]
//                const int iRB_data[]
//                double bounds_data[]
//                int bounds_size[2]
// Return Type  : void
//
static void getHalfMaxBounds(const double y[1250], const int iPk_data[], const
  int iPk_size[1], const double base_data[], const int iLB_data[], const int
  iRB_data[], double bounds_data[], int bounds_size[2])
{
  int iLeft;
  int iRight;
  int i;
  double refHeight;
  bounds_size[0] = iPk_size[0];
  bounds_size[1] = 2;
  iLeft = iPk_size[0] << 1;
  for (iRight = 0; iRight < iLeft; iRight++) {
    bounds_data[iRight] = 0.0;
  }

  for (i = 0; i < iPk_size[0]; i++) {
    refHeight = (y[iPk_data[i] - 1] + base_data[i]) / 2.0;
    iLeft = iPk_data[i];
    findLeftIntercept(y, &iLeft, iLB_data[i], refHeight);
    if (iLeft < iLB_data[i]) {
      bounds_data[i] = 1.0 + ((double)iLB_data[i] - 1.0);
    } else {
      bounds_data[i] = linterp(1.0 + ((double)iLeft - 1.0), 1.0 + ((double)
        (iLeft + 1) - 1.0), y[iLeft - 1], y[iLeft], y[iPk_data[i] - 1],
        base_data[i]);
    }

    iRight = iPk_data[i];
    findRightIntercept(y, &iRight, iRB_data[i], refHeight);
    if (iRight > iRB_data[i]) {
      bounds_data[i + bounds_size[0]] = 1.0 + ((double)iRB_data[i] - 1.0);
    } else {
      bounds_data[i + bounds_size[0]] = linterp(1.0 + ((double)iRight - 1.0),
        1.0 + ((double)(iRight - 1) - 1.0), y[iRight - 1], y[iRight - 2],
        y[iPk_data[i] - 1], base_data[i]);
    }
  }
}

//
// Arguments    : const double yTemp[1250]
//                const int iPeak_data[]
//                const int iPeak_size[1]
//                const int iFinite_data[]
//                const int iFinite_size[1]
//                const int iInflect_data[]
//                int iBase_data[]
//                int iBase_size[1]
//                int iSaddle_data[]
//                int iSaddle_size[1]
// Return Type  : void
//
static void getLeftBase(const double yTemp[1250], const int iPeak_data[], const
  int iPeak_size[1], const int iFinite_data[], const int iFinite_size[1], const
  int iInflect_data[], int iBase_data[], int iBase_size[1], int iSaddle_data[],
  int iSaddle_size[1])
{
  int n;
  int i;
  double peak_data[1250];
  double valley_data[1250];
  int iValley_data[1250];
  int j;
  int k;
  double v;
  int iv;
  double p;
  int isv;
  iBase_size[0] = (short)iPeak_size[0];
  n = (short)iPeak_size[0];
  for (i = 0; i < n; i++) {
    iBase_data[i] = 0;
  }

  iSaddle_size[0] = (short)iPeak_size[0];
  n = (short)iPeak_size[0];
  for (i = 0; i < n; i++) {
    iSaddle_data[i] = 0;
  }

  n = (short)iFinite_size[0];
  for (i = 0; i < n; i++) {
    peak_data[i] = 0.0;
  }

  n = (short)iFinite_size[0];
  for (i = 0; i < n; i++) {
    valley_data[i] = 0.0;
  }

  n = (short)iFinite_size[0];
  for (i = 0; i < n; i++) {
    iValley_data[i] = 0;
  }

  n = -1;
  i = 0;
  j = 0;
  k = 0;
  v = rtNaN;
  iv = 1;
  while (k + 1 <= iPeak_size[0]) {
    while (iInflect_data[i] != iFinite_data[j]) {
      v = yTemp[iInflect_data[i] - 1];
      iv = iInflect_data[i];
      if (rtIsNaN(yTemp[iInflect_data[i] - 1])) {
        n = -1;
      } else {
        while ((n + 1 > 0) && (valley_data[n] > v)) {
          n--;
        }
      }

      i++;
    }

    p = yTemp[iInflect_data[i] - 1];
    while ((n + 1 > 0) && (peak_data[n] < p)) {
      if (valley_data[n] < v) {
        v = valley_data[n];
        iv = iValley_data[n];
      }

      n--;
    }

    isv = iv;
    while ((n + 1 > 0) && (peak_data[n] <= p)) {
      if (valley_data[n] < v) {
        v = valley_data[n];
        iv = iValley_data[n];
      }

      n--;
    }

    n++;
    peak_data[n] = yTemp[iInflect_data[i] - 1];
    valley_data[n] = v;
    iValley_data[n] = iv;
    if (iInflect_data[i] == iPeak_data[k]) {
      iBase_data[k] = iv;
      iSaddle_data[k] = isv;
      k++;
    }

    i++;
    j++;
  }
}

//
// Arguments    : const double yTemp[1250]
//                const int iPk_data[]
//                const int iPk_size[1]
//                const int iFin_data[]
//                const int iFin_size[1]
//                const int iInflect_data[]
//                const int iInflect_size[1]
//                double peakBase_data[]
//                int peakBase_size[1]
//                int iLeftSaddle_data[]
//                int iLeftSaddle_size[1]
//                int iRightSaddle_data[]
//                int iRightSaddle_size[1]
// Return Type  : void
//
static void getPeakBase(const double yTemp[1250], const int iPk_data[], const
  int iPk_size[1], const int iFin_data[], const int iFin_size[1], const int
  iInflect_data[], const int iInflect_size[1], double peakBase_data[], int
  peakBase_size[1], int iLeftSaddle_data[], int iLeftSaddle_size[1], int
  iRightSaddle_data[], int iRightSaddle_size[1])
{
  int iLeftBase_data[1250];
  int iLeftBase_size[1];
  int tmp_size[1];
  int loop_ub;
  int i5;
  int tmp_data[1250];
  int b_tmp_size[1];
  int b_tmp_data[1250];
  int c_tmp_size[1];
  int c_tmp_data[1250];
  int iRightBase_data[1250];
  short csz_idx_0;
  getLeftBase(yTemp, iPk_data, iPk_size, iFin_data, iFin_size, iInflect_data,
              iLeftBase_data, iLeftBase_size, iLeftSaddle_data, iLeftSaddle_size);
  tmp_size[0] = iPk_size[0];
  loop_ub = iPk_size[0];
  for (i5 = 0; i5 < loop_ub; i5++) {
    tmp_data[i5] = iPk_data[i5];
  }

  b_flipud(tmp_data, tmp_size);
  b_tmp_size[0] = iFin_size[0];
  loop_ub = iFin_size[0];
  for (i5 = 0; i5 < loop_ub; i5++) {
    b_tmp_data[i5] = iFin_data[i5];
  }

  b_flipud(b_tmp_data, b_tmp_size);
  c_tmp_size[0] = iInflect_size[0];
  loop_ub = iInflect_size[0];
  for (i5 = 0; i5 < loop_ub; i5++) {
    c_tmp_data[i5] = iInflect_data[i5];
  }

  b_flipud(c_tmp_data, c_tmp_size);
  getLeftBase(yTemp, tmp_data, tmp_size, b_tmp_data, b_tmp_size, c_tmp_data,
              iRightBase_data, c_tmp_size, iRightSaddle_data, iRightSaddle_size);
  b_flipud(iRightBase_data, c_tmp_size);
  b_flipud(iRightSaddle_data, iRightSaddle_size);
  if (iLeftBase_size[0] <= c_tmp_size[0]) {
    csz_idx_0 = (short)iLeftBase_size[0];
    peakBase_size[0] = (short)iLeftBase_size[0];
  } else {
    csz_idx_0 = (short)c_tmp_size[0];
    peakBase_size[0] = (short)c_tmp_size[0];
  }

  for (loop_ub = 0; loop_ub + 1 <= csz_idx_0; loop_ub++) {
    if ((yTemp[iLeftBase_data[loop_ub] - 1] > yTemp[iRightBase_data[loop_ub] - 1])
        || rtIsNaN(yTemp[iRightBase_data[loop_ub] - 1])) {
      peakBase_data[loop_ub] = yTemp[iLeftBase_data[loop_ub] - 1];
    } else {
      peakBase_data[loop_ub] = yTemp[iRightBase_data[loop_ub] - 1];
    }
  }
}

//
// Arguments    : const double y[1250]
//                const int iPk_data[]
//                const int iPk_size[1]
//                const double pbPk_data[]
//                const int pbPk_size[1]
//                int iLB_data[]
//                int iLB_size[1]
//                int iRB_data[]
//                int iRB_size[1]
//                double wxPk_data[]
//                int wxPk_size[2]
// Return Type  : void
//
static void getPeakWidth(const double y[1250], const int iPk_data[], const int
  iPk_size[1], const double pbPk_data[], const int pbPk_size[1], int iLB_data[],
  int iLB_size[1], int iRB_data[], int iRB_size[1], double wxPk_data[], int
  wxPk_size[2])
{
  int loop_ub;
  int i11;
  double base_data[1250];
  if (iPk_size[0] == 0) {
    iLB_size[0] = 0;
    iRB_size[0] = 0;
  } else {
    loop_ub = pbPk_size[0];
    for (i11 = 0; i11 < loop_ub; i11++) {
      base_data[i11] = pbPk_data[i11];
    }
  }

  getHalfMaxBounds(y, iPk_data, iPk_size, base_data, iLB_data, iRB_data,
                   wxPk_data, wxPk_size);
}

//
// Arguments    : int idx_data[]
//                int idx_size[1]
// Return Type  : void
//
static void keepAtMostNpPeaks(int idx_data[], int idx_size[1])
{
  int b_idx_data[2500];
  int i12;
  if (idx_size[0] > 1250) {
    memcpy(&b_idx_data[0], &idx_data[0], 1250U * sizeof(int));
    idx_size[0] = 1250;
    for (i12 = 0; i12 < 1250; i12++) {
      idx_data[i12] = b_idx_data[i12];
    }
  }
}

//
// Arguments    : double xa
//                double xb
//                double ya
//                double yb
//                double yc
//                double bc
// Return Type  : double
//
static double linterp(double xa, double xb, double ya, double yb, double yc,
                      double bc)
{
  double xc;
  xc = xa + (xb - xa) * (0.5 * (yc + bc) - ya) / (yb - ya);
  if (rtIsNaN(xc)) {
    if (rtIsInf(bc)) {
      xc = 0.5 * (xa + xb);
    } else {
      xc = xb;
    }
  }

  return xc;
}

//
// Arguments    : const double x_data[]
//                const int x_size[1]
//                const boolean_T idx_data[]
//                const int idx_size[1]
//                double b_x_data[]
//                int b_x_size[1]
// Return Type  : void
//
static void nullAssignment(const double x_data[], const int x_size[1], const
  boolean_T idx_data[], const int idx_size[1], double b_x_data[], int b_x_size[1])
{
  int loop_ub;
  int i8;
  b_x_size[0] = x_size[0];
  loop_ub = x_size[0];
  for (i8 = 0; i8 < loop_ub; i8++) {
    b_x_data[i8] = x_data[i8];
  }

  b_nullAssignment(b_x_data, b_x_size, idx_data, idx_size);
}

//
// Arguments    : const double a[1250]
//                double y[1250]
// Return Type  : void
//
static void power(const double a[1250], double y[1250])
{
  int k;
  for (k = 0; k < 1250; k++) {
    y[k] = a[k] * a[k];
  }
}

//
// Arguments    : const double x_data[]
//                const int x_size[1]
//                const double y_data[]
//                double z_data[]
//                int z_size[1]
// Return Type  : void
//
static void rdivide(const double x_data[], const int x_size[1], const double
                    y_data[], double z_data[], int z_size[1])
{
  int loop_ub;
  int i9;
  z_size[0] = x_size[0];
  loop_ub = x_size[0];
  for (i9 = 0; i9 < loop_ub; i9++) {
    z_data[i9] = x_data[i9] / y_data[i9];
  }
}

//
// Arguments    : const double y[1250]
//                const int iFinite_data[]
//                const int iFinite_size[1]
//                int iPk_data[]
//                int iPk_size[1]
// Return Type  : void
//
static void removeSmallPeaks(const double y[1250], const int iFinite_data[],
  const int iFinite_size[1], int iPk_data[], int iPk_size[1])
{
  int nPk;
  int k;
  double b_y;
  nPk = 0;
  for (k = 0; k + 1 <= iFinite_size[0]; k++) {
    if (y[iFinite_data[k] - 1] > rtMinusInf) {
      if ((y[iFinite_data[k] - 2] > y[iFinite_data[k]]) || rtIsNaN
          (y[iFinite_data[k]])) {
        b_y = y[iFinite_data[k] - 2];
      } else {
        b_y = y[iFinite_data[k]];
      }

      if (y[iFinite_data[k] - 1] - b_y >= 0.0) {
        nPk++;
        iPk_data[nPk - 1] = iFinite_data[k];
      }
    }
  }

  if (1 > nPk) {
    iPk_size[0] = 0;
  } else {
    iPk_size[0] = nPk;
  }
}

//
// Arguments    : const double x[16]
// Return Type  : double
//
static double sum(const double x[16])
{
  double y;
  int k;
  y = x[0];
  for (k = 0; k < 15; k++) {
    y += x[k + 1];
  }

  return y;
}

//
// Arguments    : const double x_data[]
//                const int x_size[1]
// Return Type  : double
//
static double trapz(const double x_data[], const int x_size[1])
{
  double z;
  int iy;
  double ylast;
  int k;
  if (x_size[0] == 0) {
    z = 0.0;
  } else {
    z = 0.0;
    iy = 0;
    ylast = x_data[0];
    for (k = 0; k <= x_size[0] - 2; k++) {
      iy++;
      z += (ylast + x_data[iy]) / 2.0;
      ylast = x_data[iy];
    }
  }

  return z;
}

//
// SJ is a potato
// Arguments    : double accX[1250]
//                double accY[1250]
//                double accZ[1250]
//                double Fs
//                double PeakAcc_data[]
//                int PeakAcc_size[1]
//                double ImpactV_data[]
//                int ImpactV_size[1]
//                double Force_data[]
//                int Force_size[1]
// Return Type  : void
//
void punchDetection(double accX[1250], double accY[1250], double accZ[1250],
                    double Fs, double PeakAcc_data[], int PeakAcc_size[1],
                    double ImpactV_data[], int ImpactV_size[1], double
                    Force_data[], int Force_size[1])
{
  int i0;
  static double accXf[1250];
  static double accYf[1250];
  double accZf[1250];
  double accelSum[1250];
  double tmp_data[1250];
  double my_data[1250];
  static double ap_data[2500];
  int ap_size[1];
  static double al_data[2500];
  int al_size[1];
  static double unusedU0_data[2500];
  int unusedU0_size[1];
  static double alo_data[2500];
  int alo_size[1];
  int b_ap_size[1];
  int n;
  boolean_T b_ap_data[2500];
  int c_ap_size[1];
  static double b_tmp_data[2499];
  boolean_T fdi_data[2500];
  int fdi_size[1];
  static emxArray_real_T_2500 b_al_data;
  static emxArray_real_T_2500 c_ap_data;
  int i;
  double vz;
  int i1;
  int ry_size[1];
  double ry_data[1250];
  int ixstart;
  double pend;
  double d0;
  int accYf_size[1];
  double vy;
  int accXf_size[1];
  double vx;
  int accZf_size[1];
  int ix;
  boolean_T exitg1;
  for (i0 = 0; i0 < 1250; i0++) {
    accX[i0] *= 9.81;
    accY[i0] *= 9.81;
    accZ[i0] *= 9.81;
  }

  filtfilt(accX, accXf);
  filtfilt(accY, accYf);
  filtfilt(accZ, accZf);
  power(accXf, accelSum);
  power(accYf, tmp_data);
  power(accZf, my_data);
  for (i0 = 0; i0 < 1250; i0++) {
    accelSum[i0] = (accelSum[i0] + tmp_data[i0]) + my_data[i0];
  }

  b_sqrt(accelSum);

  //  Peak Detection
  findpeaks(accYf, ap_data, ap_size, al_data, al_size, unusedU0_data,
            unusedU0_size, alo_data, alo_size);
  b_ap_size[0] = ap_size[0];
  n = ap_size[0];
  for (i0 = 0; i0 < n; i0++) {
    b_ap_data[i0] = (ap_data[i0] < 140.0);
  }

  b_nullAssignment(al_data, al_size, b_ap_data, b_ap_size);
  alo_size[0] = al_size[0];
  n = al_size[0];
  for (i0 = 0; i0 < n; i0++) {
    alo_data[i0] = al_data[i0];
  }

  n = al_size[0];
  for (i0 = 0; i0 < n; i0++) {
    al_data[i0] /= Fs;
  }

  c_ap_size[0] = ap_size[0];
  n = ap_size[0];
  for (i0 = 0; i0 < n; i0++) {
    b_ap_data[i0] = (ap_data[i0] < 140.0);
  }

  b_nullAssignment(ap_data, ap_size, b_ap_data, c_ap_size);
  b_diff(al_data, al_size, b_tmp_data, unusedU0_size);
  n = unusedU0_size[0];
  for (i0 = 0; i0 < n; i0++) {
    fdi_data[i0] = (b_tmp_data[i0] < 0.2);
  }

  unusedU0_data[0] = 0.0;
  n = unusedU0_size[0];
  for (i0 = 0; i0 < n; i0++) {
    unusedU0_data[i0 + 1] = fdi_data[i0];
  }

  fdi_size[0] = 1 + unusedU0_size[0];
  n = 1 + unusedU0_size[0];
  for (i0 = 0; i0 < n; i0++) {
    fdi_data[i0] = (unusedU0_data[i0] != 0.0);
  }

  nullAssignment(al_data, al_size, fdi_data, fdi_size, b_al_data.data,
                 b_al_data.size);
  nullAssignment(ap_data, ap_size, fdi_data, fdi_size, c_ap_data.data,
                 c_ap_data.size);
  b_nullAssignment(alo_data, alo_size, fdi_data, fdi_size);
  ImpactV_size[0] = alo_size[0];
  n = alo_size[0];
  for (i0 = 0; i0 < n; i0++) {
    ImpactV_data[i0] = 0.0;
  }

  PeakAcc_size[0] = alo_size[0];
  n = alo_size[0];
  for (i0 = 0; i0 < n; i0++) {
    PeakAcc_data[i0] = 0.0;
  }

  //  Velocity calculato
  for (i = 0; i < alo_size[0]; i++) {
    //  Veocity Calculation start
    vz = alo_data[i] - 0.4 * Fs;
    if (vz > alo_data[i]) {
      i0 = 0;
      i1 = 0;
    } else {
      i0 = (int)vz - 1;
      i1 = (int)alo_data[i];
    }

    ry_size[0] = i1 - i0;
    n = i1 - i0;
    for (i1 = 0; i1 < n; i1++) {
      ry_data[i1] = accYf[i0 + i1];
    }

    b_abs(ry_data, ry_size, tmp_data, unusedU0_size);
    rdivide(ry_data, ry_size, tmp_data, my_data, unusedU0_size);
    n = unusedU0_size[0];
    for (ixstart = 0; ixstart < n; ixstart++) {
      vz = my_data[ixstart];
      if (my_data[ixstart] < 0.0) {
        vz = 0.0;
      }

      my_data[ixstart] = vz;
    }

    pend = sum(*(double (*)[16])&my_data[unusedU0_size[0] - 16]);
    vz = alo_data[i] - 0.4 * Fs;
    d0 = alo_data[i] - pend;
    if (vz > d0) {
      i0 = 0;
      i1 = 0;
    } else {
      i0 = (int)vz - 1;
      i1 = (int)d0;
    }

    accYf_size[0] = i1 - i0;
    n = i1 - i0;
    for (i1 = 0; i1 < n; i1++) {
      tmp_data[i1] = accYf[i0 + i1];
    }

    vz = trapz(tmp_data, accYf_size);
    vy = vz / Fs;
    vz = alo_data[i] - 0.4 * Fs;
    d0 = alo_data[i] - pend;
    if (vz > d0) {
      i0 = 0;
      i1 = 0;
    } else {
      i0 = (int)vz - 1;
      i1 = (int)d0;
    }

    accXf_size[0] = i1 - i0;
    n = i1 - i0;
    for (i1 = 0; i1 < n; i1++) {
      tmp_data[i1] = accXf[i0 + i1];
    }

    vz = trapz(tmp_data, accXf_size);
    vx = vz / Fs;
    vz = alo_data[i] - 0.4 * Fs;
    d0 = alo_data[i] - pend;
    if (vz > d0) {
      i0 = 0;
      i1 = 0;
    } else {
      i0 = (int)vz - 1;
      i1 = (int)d0;
    }

    accZf_size[0] = i1 - i0;
    n = i1 - i0;
    for (i1 = 0; i1 < n; i1++) {
      tmp_data[i1] = accZf[i0 + i1];
    }

    vz = trapz(tmp_data, accZf_size);
    vz /= Fs;
    ImpactV_data[i] = std::sqrt((vy * vy + vx * vx) + vz * vz);

    // Velocity calculation end
    vz = alo_data[i] - 0.4 * Fs;
    d0 = alo_data[i] - 0.05 * Fs;
    if (vz > d0) {
      i0 = 1;
      i1 = 1;
    } else {
      i0 = (int)vz;
      i1 = (int)d0 + 1;
    }

    ixstart = 1;
    n = i1 - i0;
    vz = accelSum[i0 - 1];
    if (i1 - i0 > 1) {
      if (rtIsNaN(accelSum[i0 - 1])) {
        ix = 2;
        exitg1 = false;
        while ((!exitg1) && (ix <= n)) {
          ixstart = ix;
          if (!rtIsNaN(accelSum[(i0 + ix) - 2])) {
            vz = accelSum[(i0 + ix) - 2];
            exitg1 = true;
          } else {
            ix++;
          }
        }
      }

      if (ixstart < i1 - i0) {
        for (ix = ixstart + 1; ix <= n; ix++) {
          if (accelSum[(i0 + ix) - 2] > vz) {
            vz = accelSum[(i0 + ix) - 2];
          }
        }
      }
    }

    PeakAcc_data[i] = vz;
  }

  Force_size[0] = alo_size[0];
  n = alo_size[0];
  for (i0 = 0; i0 < n; i0++) {
    Force_data[i0] = 54.4125 * ImpactV_data[i0] - 28.3243;
  }
}

//
// Arguments    : void
// Return Type  : void
//
void punchDetection_initialize()
{
  rt_InitInfAndNaN(8U);
}

//
// Arguments    : void
// Return Type  : void
//
void punchDetection_terminate()
{
  // (no terminate code required)
}

//
// File trailer for punchDetection.cpp
//
// [EOF]
//
