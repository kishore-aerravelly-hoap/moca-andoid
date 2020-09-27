package com.pearl.books.additionalinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.pearl.books.AdditionalInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class AdditionalInfoList.
 */
public class AdditionalInfoList implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The additional info model. */
    List<AdditionalInfo> additionalInfoModel;

    /**
     * Instantiates a new additional info list.
     */
    public AdditionalInfoList() {
	additionalInfoModel = new ArrayList<AdditionalInfo>();
    }

    /**
     * Gets the additional info model.
     *
     * @return the additional info model
     */
    public List<AdditionalInfo> getAdditionalInfoModel() {
	return additionalInfoModel;
    }

    /**
     * Sets the additional info model.
     *
     * @param additionalInfoModel the new additional info model
     */
    public void setAdditionalInfoModel(List<AdditionalInfo> additionalInfoModel) {
	this.additionalInfoModel = additionalInfoModel;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "AdditionalInfoList [additionalInfoModel=" + additionalInfoModel
		+ "]";
    }

    /*
     * public boolean hasImageDownloadableObjects(int pageNo) throws
     * ImageNotDownloadedException{ Logger.warn(tag,
     * "in hasimagedownloadable method"); boolean result = false; for
     * (AdditionalInfo infoList : additionalInfolist) { Logger.warn(tag,
     * "current page num in addinfolist is:"+pageNo); Logger.warn(tag,
     * "page num from object is:"+infoList.getPage());
     * if(infoList.getPage().equalsIgnoreCase(pageNo+"") ||
     * infoList.getPage().equalsIgnoreCase(pageNo+"")){ List<DownloadObject>
     * imageList = infoList.getImages(); if(imageList.size() > 0 ){ for(int i=0;
     * i<imageList.size(); i++){ Logger.warn(tag,
     * "---status for :"+imageList.get
     * (i).getName()+" is:"+imageList.get(i).getStatus());
     * if(imageList.get(i).getStatus().equals(DownloadStatus.NOT_DONE)){ throw
     * new ImageNotDownloadedException("Image not downloaded"); } else result =
     * true; } break; }else result = false; } } return result; }
     * 
     * public boolean hasVideoDownloadableObjects(int pageNo) throws
     * VideoNotDownloadedException{ Logger.warn(tag,
     * "in hasVideodownloadable method"); boolean result = false; for
     * (AdditionalInfo infoList : additionalInfolist) { Logger.warn(tag,
     * "current page num in addinfolist is:"+pageNo); Logger.warn(tag,
     * "page num from object is:"+infoList.getId());
     * if(infoList.getPage().equalsIgnoreCase(pageNo+"")){ List<DownloadObject>
     * videoList = infoList.getVideos(); if(videoList.size() > 0 ){ for(int i=0;
     * i<videoList.size(); i++){ Logger.warn(tag,
     * "---status for :"+videoList.get
     * (i).getName()+" is:"+videoList.get(i).getStatus());
     * if(videoList.get(i).getStatus().equals(DownloadStatus.NOT_DONE)){ throw
     * new VideoNotDownloadedException("Image not downloaded"); } else result =
     * true; } break; }else result = false; } } return result; }
     */
}
