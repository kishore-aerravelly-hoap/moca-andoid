package com.pearl.activities;


import java.io.File;
import java.io.FilenameFilter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.pearl.R;
import com.pearl.application.VegaConstants;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageGalleryActivity.
 */
public class ImageGalleryActivity extends BaseActivity implements ViewFactory {

    /** The m urls. */
    private Uri[] mUrls;
    
    /** The m files. */
    String[] mFiles = null;
    
    /** The bundle. */
    Bundle bundle;
    
    /** The page no. */
    int pageNo;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.image_gallery);
	setFinishOnTouchOutside(true);
	bundle = this.getIntent().getExtras();
	if (bundle != null) {
	    pageNo = bundle.getInt(VegaConstants.PAGE_NUM);
	} else {
	    Logger.error(tag, "Bundle is null");
	    pageNo = 0;
	}
	final Gallery gallery = (Gallery) findViewById(R.id.gallery);

	final File imagesFolder = new File(appData.getImageGalleryPathForPage(pageNo));
	if (!imagesFolder.exists()) {
	    new AlertDialog.Builder(this).setTitle("").setMessage("No imges")
	    .show();
	} else {
	    if (imagesFolder.listFiles().length != 0) {
		Logger.warn(tag, "images is:" + imagesFolder.listFiles().length);
		final File[] imagelist = imagesFolder.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
			return name.endsWith(".jpg")
				|| name.endsWith(".png") || name
				.endsWith(".jpeg");
		    }
		});
		mFiles = new String[imagelist.length];

		for (int i = 0; i < imagelist.length; i++) {
		    mFiles[i] = imagelist[i].getAbsolutePath();
		}
		mUrls = new Uri[mFiles.length];

		for (int i = 0; i < mFiles.length; i++) {
		    mUrls[i] = Uri.parse(mFiles[i]);
		}

		gallery.setAdapter(new ImageAdapter(this));

		final ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		imageSwitcher.setFactory(this);
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
			android.R.anim.fade_in));
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
			this, android.R.anim.fade_out));

		imageSwitcher.setImageURI(mUrls[0]);

		gallery.setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView parent, View v,
			    int position, long id) {
			imageSwitcher.setImageURI(mUrls[position]);
		    }
		});
	    } else {
		Toast.makeText(this, R.string.there_are_no_images,
			toastDisplayTime);
	    }

	}
    }

    /* (non-Javadoc)
     * @see android.widget.ViewSwitcher.ViewFactory#makeView()
     */
    @Override
    public View makeView() {
	final ImageView imageView = new ImageView(this);
	imageView.setBackgroundColor(0xFF000000);
	imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
		LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	return imageView;
    }

    /**
     * The Class ImageAdapter.
     */
    public class ImageAdapter extends BaseAdapter {
	
	/** The context. */
	private final Context context;
	
	/** The item background. */
	private final int itemBackground;

	/**
	 * Instantiates a new image adapter.
	 *
	 * @param c the c
	 */
	public ImageAdapter(Context c) {
	    context = c;
	    // ---setting the style---
	    final TypedArray a = obtainStyledAttributes(R.styleable.Gallery);
	    itemBackground = a.getResourceId(
		    R.styleable.Gallery_android_galleryItemBackground, 0);
	    a.recycle();
	}

	// ---returns the number of images---
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
	    return mUrls.length;
	}

	// ---returns the ID of an item---
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
	    return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
	    return position;
	}

	// ---returns an ImageView view---
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    final ImageView imageView = new ImageView(context);
	    // imageView.setImageResource(imageIDs[position]);
	    imageView.setImageURI(mUrls[position]);
	    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	    imageView.setLayoutParams(new Gallery.LayoutParams(150, 120));
	    imageView.setBackgroundResource(itemBackground);
	    return imageView;
	}
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "Image_selected";
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
     */
    @Override
    public void onNetworkAvailable() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
     */
    @Override
    public void onNetworkUnAvailable() {
	// TODO Auto-generated method stub

    }
}
