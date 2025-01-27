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

import org.jwildfire.base.Tools;
import org.jwildfire.create.tina.base.Layer;
import org.jwildfire.create.tina.base.XForm;
import org.jwildfire.create.tina.base.XYZPoint;

import static org.jwildfire.base.mathlib.MathLib.max;
import static org.jwildfire.base.mathlib.MathLib.min;

public class PostCropFunc extends VariationFunc implements SupportsGPU {
  private static final long serialVersionUID = 1L;

  private static final String PARAM_LEFT = "left";
  private static final String PARAM_RIGHT = "right";
  private static final String PARAM_TOP = "top";
  private static final String PARAM_BOTTOM = "bottom";
  private static final String PARAM_SCATTER_AREA = "scatter_area";
  private static final String PARAM_ZERO = "zero";

  private static final String[] paramNames = {PARAM_LEFT, PARAM_RIGHT, PARAM_TOP, PARAM_BOTTOM, PARAM_SCATTER_AREA, PARAM_ZERO};

  private double left = -1.0;
  private double top = -1.0;
  private double right = 1.0;
  private double bottom = 1.0;
  private double scatter_area = 0.0;
  private int zero = 0;

  @Override
  public void transform(FlameTransformationContext pContext, XForm pXForm, XYZPoint pAffineTP, XYZPoint pVarTP, double pAmount) {
    // post_crop by Xyrus02, http://xyrus02.deviantart.com/art/Crop-Plugin-Updated-169958881
    double x = pVarTP.x;
    double y = pVarTP.y;
    if (((x < xmin) || (x > xmax) || (y < ymin) || (y > ymax)) && (zero != 0)) {
      pVarTP.x = pVarTP.y = 0;
      pVarTP.doHide = true;
      return;
    } else {
      pVarTP.doHide = false;
      if (x < xmin)
        x = xmin + pContext.random() * w;
      else if (x > xmax)
        x = xmax - pContext.random() * w;
      if (y < ymin)
        y = ymin + pContext.random() * h;
      else if (y > ymax)
        y = ymax - pContext.random() * h;
    }
    pVarTP.x = pAmount * x;
    pVarTP.y = pAmount * y;
  }

  @Override
  public String[] getParameterNames() {
    return paramNames;
  }

  @Override
  public Object[] getParameterValues() {
    return new Object[]{left, right, top, bottom, scatter_area, zero};
  }

  @Override
  public void setParameter(String pName, double pValue) {
    if (PARAM_LEFT.equalsIgnoreCase(pName))
      left = pValue;
    else if (PARAM_RIGHT.equalsIgnoreCase(pName))
      right = pValue;
    else if (PARAM_TOP.equalsIgnoreCase(pName))
      top = pValue;
    else if (PARAM_BOTTOM.equalsIgnoreCase(pName))
      bottom = pValue;
    else if (PARAM_SCATTER_AREA.equalsIgnoreCase(pName))
      scatter_area = limitVal(pValue, -1.0, 1.0);
    else if (PARAM_ZERO.equalsIgnoreCase(pName))
      zero = limitIntVal(Tools.FTOI(pValue), 0, 1);
    else
      throw new IllegalArgumentException(pName);
  }

  @Override
  public String getName() {
    return "post_crop";
  }

  private double xmin, xmax, ymin, ymax, w, h;

  @Override
  public void init(FlameTransformationContext pContext, Layer pLayer, XForm pXForm, double pAmount) {
    xmin = min(left, right);
    ymin = min(top, bottom);
    xmax = max(left, right);
    ymax = max(top, bottom);
    w = (xmax - xmin) * 0.5 * scatter_area;
    h = (ymax - ymin) * 0.5 * scatter_area;
  }

  @Override
  public int getPriority() {
    return 1;
  }

  @Override
  public VariationFuncType[] getVariationTypes() {
    return new VariationFuncType[]{VariationFuncType.VARTYPE_2D, VariationFuncType.VARTYPE_CROP, VariationFuncType.VARTYPE_POST, VariationFuncType.VARTYPE_SUPPORTS_GPU};
  }
  @Override
  public String getGPUCode(FlameTransformationContext context) {
    return   "    float xmin = fminf( __post_crop_left ,  __post_crop_right );"
    		+"    float ymin = fminf( __post_crop_top ,  __post_crop_bottom );"
    		+"    float xmax = fmaxf( __post_crop_left ,  __post_crop_right );"
    		+"    float ymax = fmaxf( __post_crop_top ,  __post_crop_bottom );"
    		+"    float w = (xmax - xmin) * 0.5 *  __post_crop_scatter_area ;"
    		+"    float h = (ymax - ymin) * 0.5 *  __post_crop_scatter_area ;"
    		+"    float x = __px;"
    		+"    float y = __py;"
    		+"    if (((x < xmin) || (x > xmax) || (y < ymin) || (y > ymax)) && ( __post_crop_zero  != 0)) {"
    		+"      __px = __py = 0;"
    		+"      __doHide = true;"
    		+"    } else {"
    		+"      __doHide = false;"
    		+"      if (x < xmin)"
    		+"        x = xmin + RANDFLOAT() * w;"
    		+"      else if (x > xmax)"
    		+"        x = xmax - RANDFLOAT() * w;"
    		+"      if (y < ymin)"
    		+"        y = ymin + RANDFLOAT() * h;"
    		+"      else if (y > ymax)"
    		+"        y = ymax - RANDFLOAT() * h;"
    		+"     __px = __post_crop * x;"
    		+"     __py = __post_crop * y;"
    		+"    }";
  }
}
