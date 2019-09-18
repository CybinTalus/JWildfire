package org.jwildfire.create.tina.variation;

import static org.jwildfire.base.mathlib.MathLib.M_2PI;
import static org.jwildfire.base.mathlib.MathLib.cos;
import static org.jwildfire.base.mathlib.MathLib.sin;

import java.util.Random;

import org.jwildfire.base.Tools;
import org.jwildfire.create.tina.base.Layer;
import org.jwildfire.create.tina.base.XForm;
import org.jwildfire.create.tina.base.XYZPoint;
import org.jwildfire.create.tina.palette.RGBColor;
import org.jwildfire.create.tina.palette.RGBPalette;

import js.glsl.G;
import js.glsl.mat2;
import js.glsl.vec2;
import js.glsl.vec3;
import js.glsl.vec4;



public class CutFingerPrintFunc  extends VariationFunc {

	/*
	 * Variation : cut_fingerprint
	 * Autor: Jesus Sosa
	 * Date: August 20, 2019
	 * Reference 
	 */



	private static final long serialVersionUID = 1L;



	private static final String PARAM_ZOOM = "zoom";
	private static final String PARAM_SEED = "seed";
	private static final String PARAM_WIDTH = "width";
	private static final String PARAM_INVERT = "invert";




	double zoom=20.0;
	private int seed = 10000;
	double time=0.0;
	double width=0.8;
	int invert=0;



	Random randomize=new Random(seed);
	
 	long last_time=System.currentTimeMillis();
 	long elapsed_time=0;
	

	private static final String[] additionalParamNames = { PARAM_ZOOM,PARAM_SEED,PARAM_WIDTH,PARAM_INVERT};


	public vec2 hash2( vec2 p )
	{
		p = new vec2( G.dot(p,new vec2(63.31,127.63)), G.dot(p,new vec2(395.467,213.799)) );
		return G.fract(G.sin(p).multiply(43141.59265)).multiply(2.0).minus(1.);
	}
	
	public vec3 getRGBColor(double xp,double yp)
	{

	    
		vec2 uv=new vec2(xp,yp).multiply(zoom);

	    vec3 color=new vec3(0.);// n  

	    double bounds = G.smoothstep(9.,10.,G.length(uv.multiply( new vec2(0.7,0.5))));

	    //cumulate information
	    double a=0.;
	    vec2 h = new vec2(G.floor(7.*seed), 0.);
	    for(int i=0; i<50; i++){
	        double s=G.sign(h.x);
	        h = hash2(h).multiply(new vec2(15.,20.));
	    	a += s*G.atan2(uv.x-h.x, uv.y-h.y);
	    }
	    
	    //comment this out for static center
	    uv = uv.plus(G.abs(hash2(h)));
	    
	    a+=G.atan2(uv.y, uv.x); //spirallic center more likely

	    //double width = 0.8; //row width
	    double p=(1.-bounds)*width; //pressure
	    double s = G.min(0.3,p); //smooth
	    double l = G.length(uv)+0.319*a; //base rings plus information
	    
	    //dist -> alternate pattern
	    double m = G.mod(l,2.);
	    double v = (1.-G.smoothstep(2.-s,2.,m))*G.smoothstep(p,p+s,m);

		return new vec3(v);
	}
 	
	  public void transform(FlameTransformationContext pContext, XForm pXForm, XYZPoint pAffineTP, XYZPoint pVarTP, double pAmount) {
		    double x = pAffineTP.x;
		    double y = pAffineTP.y;
		    vec3 color=getRGBColor(x,y);
		    pVarTP.doHide=false;
		    if(invert==0)
		    {
		      if (color.x==0&& color.y==0 && color.z==0)
		      { pVarTP.x=0;
		        pVarTP.y=0;
		        pVarTP.doHide = true;
		        return;
		      }
		    } else
		    {
			      if (color.x>0 && color.y>0 && color.z>0)
			      { pVarTP.x=0;
			        pVarTP.y=0;
			        pVarTP.doHide = true;
			        return;
			      }
		    }
		    pVarTP.x = pAmount * x;
		    pVarTP.y = pAmount * y;
		    if (pContext.isPreserveZCoordinate()) {
		      pVarTP.z += pAmount * pAffineTP.z;
		    }
		  }
	

	public String getName() {
		return "cut_fingerprint";
	}

	public String[] getParameterNames() {
		return additionalParamNames;
	}


	public Object[] getParameterValues() { //re_min,re_max,im_min,im_max,
		return (new Object[] { zoom,seed,width,invert});
	}

	public void setParameter(String pName, double pValue) {
		if (pName.equalsIgnoreCase(PARAM_ZOOM)) {
			zoom = Tools.limitValue(pValue, 1.0 , 100.0);
		}
		else if (pName.equalsIgnoreCase(PARAM_SEED)) {
			   seed =   (int)pValue;
		       randomize=new Random(seed);
		          long current_time = System.currentTimeMillis();
		          elapsed_time += (current_time - last_time);
		          last_time = current_time;
		          time = (double) (elapsed_time / 1000.0);
		}
		else if (pName.equalsIgnoreCase(PARAM_WIDTH)) {
			width =Math.abs(Tools.limitValue(pValue, 0.02 , 1.0));
		}
		else if (pName.equalsIgnoreCase(PARAM_INVERT)) {
			invert =(int)Tools.limitValue(pValue, 0 , 1);
		}
		else
		      throw new IllegalArgumentException(pName);
	}

	@Override
	public boolean dynamicParameterExpansion() {
		return true;
	}

	@Override
	public boolean dynamicParameterExpansion(String pName) {
		// preset_id doesn't really expand parameters, but it changes them; this will make them refresh
		return true;
	}	
	
}
