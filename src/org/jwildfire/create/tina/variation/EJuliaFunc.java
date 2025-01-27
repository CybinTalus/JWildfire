/*
  JWildfire - an image and animation processor written in Java 
  Copyright (C) 1995-2021 Andreas Maschke

  This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser 
  General Public License as published by the Free Software Foundation; either version 2.1 of the 
  License, or (at your option) any later version.
 
  This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License along with this software; 
  if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jwildfire.create.tina.variation;

import org.jwildfire.base.Tools;
import org.jwildfire.create.tina.base.Layer;
import org.jwildfire.create.tina.base.XForm;
import org.jwildfire.create.tina.base.XYZPoint;

import static org.jwildfire.base.mathlib.MathLib.*;

public class EJuliaFunc extends VariationFunc implements SupportsGPU {
  private static final long serialVersionUID = 1L;

  private static final String PARAM_POWER = "power";

  private static final String[] paramNames = {PARAM_POWER};

  private int power = 2;

  //Taking the square root of numbers close to zero is dangerous.  If x is negative
  //due to floating point errors we get NaN results.
  private double sqrt_safe(double x) {
    if (x <= 0.0)
      return 0.0;
    return sqrt(x);
  }

  @Override
  public void transform(FlameTransformationContext pContext, XForm pXForm, XYZPoint pAffineTP, XYZPoint pVarTP, double pAmount) {
    // eJulia by Michael Faber, http://michaelfaber.deviantart.com/art/eSeries-306044892
    double r2 = pAffineTP.y * pAffineTP.y + pAffineTP.x * pAffineTP.x;
    double tmp2;
    double x;
    if (_sign == 1)
      x = pAffineTP.x;
    else {
      r2 = 1.0 / r2;
      x = pAffineTP.x * r2;
    }

    double tmp = r2 + 1.0;
    tmp2 = 2.0 * x;
    double xmax = (sqrt_safe(tmp + tmp2) + sqrt_safe(tmp - tmp2)) * 0.5;
    if (xmax < 1.0)
      xmax = 1.0;
    double sinhmu, coshmu, sinnu, cosnu;

    double mu = acosh(xmax); //  mu > 0
    double t = x / xmax;
    if (t > 1.0)
      t = 1.0;
    else if (t < -1.0)
      t = -1.0;

    double nu = acos(t); // -Pi < nu < Pi
    if (pAffineTP.y < 0)
      nu *= -1.0;

    nu = nu / power + M_2PI / power * floor(pContext.random() * power);
    mu /= power;

    sinhmu = sinh(mu);
    coshmu = cosh(mu);

    sinnu = sin(nu);
    cosnu = cos(nu);
    pVarTP.x += pAmount * coshmu * cosnu;
    pVarTP.y += pAmount * sinhmu * sinnu;

    if (pContext.isPreserveZCoordinate()) {
      pVarTP.z += pAmount * pAffineTP.z;
    }
  }

  @Override
  public String[] getParameterNames() {
    return paramNames;
  }

  @Override
  public Object[] getParameterValues() {
    return new Object[]{power};
  }

  @Override
  public void setParameter(String pName, double pValue) {
    if (PARAM_POWER.equalsIgnoreCase(pName))
      power = Tools.FTOI(pValue);
    else
      throw new IllegalArgumentException(pName);
  }

  @Override
  public String getName() {
    return "eJulia";
  }

  private int _sign;

  @Override
  public void init(FlameTransformationContext pContext, Layer pLayer, XForm pXForm, double pAmount) {
    _sign = 1;
    if (power < 0)
      _sign = -1;
  }

  @Override
  public VariationFuncType[] getVariationTypes() {
    return new VariationFuncType[]{VariationFuncType.VARTYPE_2D, VariationFuncType.VARTYPE_SUPPORTS_GPU};
  }

  @Override
  public String getGPUCode(FlameTransformationContext context) {
    return "int _sign = 1;\n"
        + "if (__eJulia_power < 0)\n"
        + "   _sign = -1;\n"
        + "float r2 = __y * __y + __x * __x;\n"
        + "float tmp2;\n"
        + "float x;\n"
        + "if (_sign == 1)\n"
        + "  x = __x;\n"
        + "else {\n"
        + "  r2 = 1.0f / r2;\n"
        + "  x = __x * r2;\n"
        + "}\n"
        + "float tmp = r2 + 1.0f;\n"
        + "tmp2 = 2.0f * x;\n"
        + "float xmax = (sqrtf_safe(tmp + tmp2) + sqrtf_safe(tmp - tmp2)) * 0.5f;\n"
        + "if (xmax < 1.0f)\n"
        + "  xmax = 1.0f;\n"
        + "float sinhmu, coshmu, sinnu, cosnu;\n"
        + "float mu = acoshf(xmax);\n"
        + "float t = x / xmax;\n"
        + "if (t > 1.0f)\n"
        + "  t = 1.0f;\n"
        + "else if (t < -1.0f)\n"
        + "  t = -1.0f;\n"
        + "float nu = acosf(t);\n"
        + "if (__y < 0)\n"
        + "  nu *= -1.0f;\n"
        + "nu = nu / __eJulia_power + 2.0f*PI / __eJulia_power * floorf(RANDFLOAT() * __eJulia_power);\n"
        + "mu /= __eJulia_power;\n"
        + "sinhmu = sinhf(mu);\n"
        + "coshmu = coshf(mu);\n"
        + "sinnu = sinf(nu);\n"
        + "cosnu = cosf(nu);\n"
        + "__px += __eJulia * coshmu * cosnu;\n"
        + "__py += __eJulia * sinhmu * sinnu;\n"
        + (context.isPreserveZCoordinate() ? "__pz += __eJulia * __z;\n" : "");
  }
}
