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
package org.jwildfire.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PrefsReader {

  private String getProperty(Properties pProperties, String pKey, String pDefaultValue) {
    String res = pProperties.getProperty(pKey, "").trim();
    return res.length() > 0 ? res : pDefaultValue;
  }

  private int getIntProperty(Properties pProperties, String pKey, int pDefaultValue) {
    String val = pProperties.getProperty(pKey, "").trim();
    return val.length() > 0 ? Tools.stringToInt(val) : pDefaultValue;
  }

  private boolean getBooleanProperty(Properties pProperties, String pKey, boolean pDefaultValue) {
    String val = pProperties.getProperty(pKey, "").trim();
    return val.length() > 0 ? val.equalsIgnoreCase("true") : pDefaultValue;
  }

  private double getDoubleProperty(Properties pProperties, String pKey, double pDefaultValue) {
    String val = pProperties.getProperty(pKey, "").trim();
    return val.length() > 0 ? Tools.stringToDouble(val) : pDefaultValue;
  }

  public void readPrefs(Prefs pPrefs) throws Exception {
    File file = new File(System.getProperty("user.home"), Prefs.PREFS_FILE);
    if (file.exists()) {
      InputStream inputStream = new FileInputStream(file);
      try {
        Properties props = new Properties();
        props.load(inputStream);
        pPrefs.setPlafStyle(getProperty(props, Prefs.KEY_GENERAL_PLAF_STYLE, pPrefs.getPlafStyle()));
        pPrefs.setPlafTheme(getProperty(props, Prefs.KEY_GENERAL_PLAF_THEME, pPrefs.getPlafTheme()));
        pPrefs.setImagePath(getProperty(props, Prefs.KEY_GENERAL_PATH_IMAGES, pPrefs.getImagePath()));
        pPrefs.setScriptPath(getProperty(props, Prefs.KEY_GENERAL_PATH_SCRIPTS, pPrefs.getScriptPath()));

        pPrefs.setTinaFlamePath(getProperty(props, Prefs.KEY_TINA_PATH_FLAMES, pPrefs.getTinaFlamePath()));
        pPrefs.setTinaRenderImageWidth(getIntProperty(props, Prefs.KEY_TINA_RENDER_IMAGE_WIDTH, pPrefs.getTinaRenderImageWidth()));
        pPrefs.setTinaRenderImageHeight(getIntProperty(props, Prefs.KEY_TINA_RENDER_IMAGE_HEIGHT, pPrefs.getTinaRenderImageHeight()));
        pPrefs.setTinaRenderMovieWidth(getIntProperty(props, Prefs.KEY_TINA_RENDER_MOVIE_WIDTH, pPrefs.getTinaRenderMovieWidth()));
        pPrefs.setTinaRenderMovieHeight(getIntProperty(props, Prefs.KEY_TINA_RENDER_MOVIE_HEIGHT, pPrefs.getTinaRenderMovieHeight()));
        pPrefs.setTinaRenderMovieFrames(getIntProperty(props, Prefs.KEY_TINA_RENDER_MOVIE_FRAMES, pPrefs.getTinaRenderMovieFrames()));
        pPrefs.setTinaRenderPreviewSpatialOversample(getIntProperty(props, Prefs.KEY_TINA_RENDER_PREVIEW_SPATIAL_OVERSAMPLE, pPrefs.getTinaRenderPreviewSpatialOversample()));
        pPrefs.setTinaRenderPreviewColorOversample(getIntProperty(props, Prefs.KEY_TINA_RENDER_PREVIEW_COLOR_OVERSAMPLE, pPrefs.getTinaRenderPreviewColorOversample()));
        pPrefs.setTinaRenderPreviewFilterRadius(getDoubleProperty(props, Prefs.KEY_TINA_RENDER_PREVIEW_FILTER_RADIUS, pPrefs.getTinaRenderPreviewFilterRadius()));
        pPrefs.setTinaRenderPreviewQuality(getIntProperty(props, Prefs.KEY_TINA_RENDER_PREVIEW_QUALITY, pPrefs.getTinaRenderPreviewQuality()));
        pPrefs.setTinaRenderNormalSpatialOversample(getIntProperty(props, Prefs.KEY_TINA_RENDER_NORMAL_SPATIAL_OVERSAMPLE, pPrefs.getTinaRenderNormalSpatialOversample()));
        pPrefs.setTinaRenderNormalColorOversample(getIntProperty(props, Prefs.KEY_TINA_RENDER_NORMAL_COLOR_OVERSAMPLE, pPrefs.getTinaRenderNormalColorOversample()));
        pPrefs.setTinaRenderNormalFilterRadius(getDoubleProperty(props, Prefs.KEY_TINA_RENDER_NORMAL_FILTER_RADIUS, pPrefs.getTinaRenderNormalFilterRadius()));
        pPrefs.setTinaRenderNormalQuality(getIntProperty(props, Prefs.KEY_TINA_RENDER_NORMAL_QUALITY, pPrefs.getTinaRenderNormalQuality()));
        pPrefs.setTinaRenderNormalHDR(getBooleanProperty(props, Prefs.KEY_TINA_RENDER_NORMAL_HDR, pPrefs.isTinaRenderNormalHDR()));
        pPrefs.setTinaRenderNormalHDRIntensityMap(getBooleanProperty(props, Prefs.KEY_TINA_RENDER_NORMAL_HDR_INTENSITY_MAP, pPrefs.isTinaRenderNormalHDRIntensityMap()));
        pPrefs.setTinaRenderHighSpatialOversample(getIntProperty(props, Prefs.KEY_TINA_RENDER_HIGH_SPATIAL_OVERSAMPLE, pPrefs.getTinaRenderHighSpatialOversample()));
        pPrefs.setTinaRenderHighColorOversample(getIntProperty(props, Prefs.KEY_TINA_RENDER_HIGH_COLOR_OVERSAMPLE, pPrefs.getTinaRenderHighColorOversample()));
        pPrefs.setTinaRenderHighFilterRadius(getDoubleProperty(props, Prefs.KEY_TINA_RENDER_HIGH_FILTER_RADIUS, pPrefs.getTinaRenderHighFilterRadius()));
        pPrefs.setTinaRenderHighQuality(getIntProperty(props, Prefs.KEY_TINA_RENDER_HIGH_QUALITY, pPrefs.getTinaRenderHighQuality()));
        pPrefs.setTinaRenderHighHDR(getBooleanProperty(props, Prefs.KEY_TINA_RENDER_HIGH_HDR, pPrefs.isTinaRenderHighHDR()));
        pPrefs.setTinaRenderHighHDRIntensityMap(getBooleanProperty(props, Prefs.KEY_TINA_RENDER_HIGH_HDR_INTENSITY_MAP, pPrefs.isTinaRenderHighHDRIntensityMap()));
        pPrefs.setTinaRenderMovieSpatialOversample(getIntProperty(props, Prefs.KEY_TINA_RENDER_MOVIE_SPATIAL_OVERSAMPLE, pPrefs.getTinaRenderMovieSpatialOversample()));
        pPrefs.setTinaRenderMovieColorOversample(getIntProperty(props, Prefs.KEY_TINA_RENDER_MOVIE_COLOR_OVERSAMPLE, pPrefs.getTinaRenderMovieColorOversample()));
        pPrefs.setTinaRenderMovieFilterRadius(getDoubleProperty(props, Prefs.KEY_TINA_RENDER_MOVIE_FILTER_RADIUS, pPrefs.getTinaRenderMovieFilterRadius()));
        pPrefs.setTinaRenderMovieQuality(getIntProperty(props, Prefs.KEY_TINA_RENDER_MOVIE_QUALITY, pPrefs.getTinaRenderMovieQuality()));
        pPrefs.setTinaRenderRealtimeQuality(getIntProperty(props, Prefs.KEY_TINA_RENDER_REALTIME_QUALITY, pPrefs.getTinaRenderRealtimeQuality()));
        pPrefs.setTinaRandomBatchSize(getIntProperty(props, Prefs.KEY_TINA_RANDOMBATCH_SIZE, pPrefs.getTinaRandomBatchSize()));
        pPrefs.setTinaRandomBatchBGColorRed(getIntProperty(props, Prefs.KEY_TINA_RANDOMBATCH_BGCOLOR_RED, pPrefs.getTinaRandomBatchBGColorRed()));
        pPrefs.setTinaRandomBatchBGColorGreen(getIntProperty(props, Prefs.KEY_TINA_RANDOMBATCH_BGCOLOR_GREEN, pPrefs.getTinaRandomBatchBGColorGreen()));
        pPrefs.setTinaRandomBatchBGColorBlue(getIntProperty(props, Prefs.KEY_TINA_RANDOMBATCH_BGCOLOR_BLUE, pPrefs.getTinaRandomBatchBGColorBlue()));
        // resolution profiles
        {
          int count = getIntProperty(props, Prefs.KEY_TINA_PROFILE_RESOLUTION_COUNT, 0);
          for (int i = 0; i < count; i++) {
            ResolutionProfile profile = new ResolutionProfile();
            pPrefs.getResolutionProfiles().add(profile);
            profile.setDefaultProfile(getBooleanProperty(props, Prefs.KEY_TINA_PROFILE_RESOLUTION_DEFAULT_PROFILE + "." + i, false));
            profile.setWidth(getIntProperty(props, Prefs.KEY_TINA_PROFILE_RESOLUTION_WIDTH + "." + i, 0));
            profile.setHeight(getIntProperty(props, Prefs.KEY_TINA_PROFILE_RESOLUTION_HEIGHT + "." + i, 0));
          }
        }
        // quality profiles
        {
          int count = getIntProperty(props, Prefs.KEY_TINA_PROFILE_QUALITY_COUNT, 0);
          for (int i = 0; i < count; i++) {
            QualityProfile profile = new QualityProfile();
            pPrefs.getQualityProfiles().add(profile);
            profile.setDefaultProfile(getBooleanProperty(props, Prefs.KEY_TINA_PROFILE_QUALITY_DEFAULT_PROFILE + "." + i, false));
            profile.setSpatialOversample(getIntProperty(props, Prefs.KEY_TINA_PROFILE_QUALITY_SPATIAL_OVERSAMPLE + "." + i, 1));
            profile.setColorOversample(getIntProperty(props, Prefs.KEY_TINA_PROFILE_QUALITY_COLOR_OVERSAMPLE + "." + i, 1));
            profile.setQuality(getIntProperty(props, Prefs.KEY_TINA_PROFILE_QUALITY_QUALITY + "." + i, 1));
            profile.setWithHDR(getBooleanProperty(props, Prefs.KEY_TINA_PROFILE_QUALITY_WITH_HDR + "." + i, false));
            profile.setWithHDRIntensityMap(getBooleanProperty(props, Prefs.KEY_TINA_PROFILE_QUALITY_WITH_HDR_INTENSITY_MAP + "." + i, false));
            profile.setCaption(getProperty(props, Prefs.KEY_TINA_PROFILE_QUALITY_CAPTION + "." + i, ""));
          }
        }
        //
        pPrefs.setSunflowScenePath(getProperty(props, Prefs.KEY_SUNFLOW_PATH_SCENES, pPrefs.getSunflowScenePath()));
        //
        setupDefaultProfiles(pPrefs);
      }
      finally {
        inputStream.close();
      }
    }
  }

  private void setupDefaultProfiles(Prefs pPrefs) {
    if (pPrefs.getResolutionProfiles().size() == 0) {
      pPrefs.getResolutionProfiles().add(new ResolutionProfile(false, 640, 480));
      pPrefs.getResolutionProfiles().add(new ResolutionProfile(true, 800, 600));
      pPrefs.getResolutionProfiles().add(new ResolutionProfile(false, 1024, 768));
      pPrefs.getResolutionProfiles().add(new ResolutionProfile(false, 1680, 1050));
      pPrefs.getResolutionProfiles().add(new ResolutionProfile(false, 1920, 1080));
    }
    if (pPrefs.getQualityProfiles().size() == 0) {
      pPrefs.getQualityProfiles().add(new QualityProfile(true, "Fast", 1, 2, 400, false, false));
      pPrefs.getQualityProfiles().add(new QualityProfile(true, "High", 1, 3, 600, false, false));
      pPrefs.getQualityProfiles().add(new QualityProfile(false, "High with HDR", 1, 3, 500, true, false));
      pPrefs.getQualityProfiles().add(new QualityProfile(false, "Very high with HDR", 2, 3, 800, true, true));
    }
  }

}
