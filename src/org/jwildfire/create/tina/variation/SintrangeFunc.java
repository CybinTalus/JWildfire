/*
  JWildfire - an image and animation processor written in Java 
  Copyright (C) 1995-2011 Andreas Maschke

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

import org.jwildfire.create.tina.base.XForm;
import org.jwildfire.create.tina.base.XYZPoint;

import static org.jwildfire.base.mathlib.MathLib.sin;
import static org.jwildfire.base.mathlib.MathLib.sqr;

public class SintrangeFunc extends VariationFunc implements SupportsGPU {
  private static final long serialVersionUID = 1L;

  private static final String PARAM_W = "w";
  private static final String[] paramNames = {PARAM_W};

  private double w = 1.0;

  @Override
  public void transform(FlameTransformationContext pContext, XForm pXForm, XYZPoint pAffineTP, XYZPoint pVarTP, double pAmount) {
    /* Sintrange from Ffey, http://ffey.deviantart.com/art/apoplugin-Sintrange-245146228 */
    double v = ((sqr(pAffineTP.x) + sqr(pAffineTP.y)) * w);
    pVarTP.x = pAmount * (sin(pAffineTP.x)) * (pAffineTP.x * pAffineTP.x + w - v);
    pVarTP.y = pAmount * (sin(pAffineTP.y)) * (pAffineTP.y * pAffineTP.y + w - v);
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
    return new Object[]{w};
  }

  @Override
  public void setParameter(String pName, double pValue) {
    if (PARAM_W.equalsIgnoreCase(pName))
      w = pValue;
    else
      throw new IllegalArgumentException(pName);
  }

  @Override
  public String getName() {
    return "sintrange";
  }

  @Override
  public VariationFuncType[] getVariationTypes() {
    return new VariationFuncType[]{VariationFuncType.VARTYPE_2D,VariationFuncType.VARTYPE_SUPPORTS_GPU};
  }
  @Override
  public String getGPUCode(FlameTransformationContext context) {
    return   "    float v = ((__x*__x + __y*__y) * __sintrange_w);"
    		+"    __px = __sintrange * (sinf(__x)) * (__x * __x + __sintrange_w - v);"
    		+"    __py = __sintrange * (sinf(__y)) * (__y * __y + __sintrange_w - v);"
            + (context.isPreserveZCoordinate() ? "__pz += __sintrange *__z;" : "");
  }
}
