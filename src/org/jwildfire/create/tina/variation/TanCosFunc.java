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

import org.jwildfire.create.tina.base.XForm;
import org.jwildfire.create.tina.base.XYZPoint;

import static org.jwildfire.base.mathlib.MathLib.*;

public class TanCosFunc extends SimpleVariationFunc implements SupportsGPU {
  private static final long serialVersionUID = 1L;

  @Override
  public void transform(FlameTransformationContext pContext, XForm pXForm, XYZPoint pAffineTP, XYZPoint pVarTP, double pAmount) {
    // tancos by Raykoid666, http://raykoid666.deviantart.com/art/plugin-pack-3-100510461?q=gallery%3ARaykoid666%2F11060240&qo=16
    double d1 = SMALL_EPSILON + sqr(pAffineTP.x) + sqr(pAffineTP.y);
    double d2 = pAmount / d1;
    pVarTP.x += d2 * (tanh(d1) * (2.0 * pAffineTP.x));
    pVarTP.y += d2 * (cos(d1) * (2.0 * pAffineTP.y));
    if (pContext.isPreserveZCoordinate()) {
      pVarTP.z += pAmount * pAffineTP.z;
    }
  }

  @Override
  public String getName() {
    return "tancos";
  }

  @Override
  public VariationFuncType[] getVariationTypes() {
    return new VariationFuncType[]{VariationFuncType.VARTYPE_2D, VariationFuncType.VARTYPE_SUPPORTS_GPU};
  }

  @Override
  public String getGPUCode(FlameTransformationContext context) {
    return "    float d1 = 1.e-6f + __x*__x + __y*__y;\n"
        + "    float d2 = __tancos / d1;\n"
        + "    __px += d2 * (tanh(d1) * (2.0 * __x));\n"
        + "    __py += d2 * (cosf(d1) * (2.0 * __y));\n"
        + (context.isPreserveZCoordinate() ? "      __pz += __tancos * __z;\n" : "");
  }
}
