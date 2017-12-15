/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.bigplan.lego.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.bigplan.lego.R;
import android.bigplan.lego.cache.AbCacheHeaderParser;
import android.bigplan.lego.cache.AbCacheResponse;
import android.bigplan.lego.cache.AbCacheUtil;
import android.bigplan.lego.cache.AbDiskBaseCache;
import android.bigplan.lego.cache.AbDiskCache.Entry;
import android.bigplan.lego.cache.image.AbBitmapResponse;
import android.bigplan.lego.cache.image.AbImageBaseCache;
import android.bigplan.lego.global.AbAppConfig;
import android.bigplan.lego.task.AbTaskItem;
import android.bigplan.lego.task.AbTaskObjectListener;
import android.bigplan.lego.task.thread.AbTaskQueue;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbImageLoader.java
 * 描述：下载图片并显示的工具类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2011-12-10 上午10:10:53
 */
public class AbImageLoader { 
	
    /** 上下文. */
    private Context context = null;
    
    /** 单例对象. */
	private static AbImageLoader imageLoader = null; 
    
    /** 缓存超时时间设置. */
    private int expiresTime;
    
    /** 请求队列. */
    private List<AbTaskQueue> taskQueueList;
    
    /** 图片缓存. */
    private AbImageBaseCache memCache;
    
    /** 磁盘缓存. */
    private AbDiskBaseCache diskCache;
    
    /**
     * 构造图片下载器.
     *
     * @param context the context
     */
    public AbImageLoader(Context context) {
    	this.context = context;
    	this.expiresTime = AbAppConfig.IMAGE_CACHE_EXPIRES_TIME;
    	this.taskQueueList = new ArrayList<AbTaskQueue>();
    	PackageInfo info = AbAppUtil.getPackageInfo(context);
    	File cacheDir = null;
    	if(!AbFileUtil.isCanUseSD()){
    		cacheDir = new File(context.getCacheDir(), info.packageName);
		}else{
			cacheDir = new File(AbFileUtil.getCacheDownloadDir(context));
		}
    	this.diskCache = new AbDiskBaseCache(cacheDir);
    	this.memCache = AbImageBaseCache.getInstance();
    } 
    
    
	/**
	 * 
	 * 获得一个实例.
	 * @param context
	 * @return
	 */
	public static AbImageLoader getInstance(Context context) {
		if (imageLoader == null) { 
			imageLoader = new AbImageLoader(context); 
        }
		return imageLoader;
	}
	
	/**
	 * 
	 * 显示这个图片.
	 * @param imageView
	 * @param url
	 * @param desiredWidth
	 * @param desiredHeight
	 */
    public void display(ImageView imageView,String url,int desiredWidth, int desiredHeight){
    	download(imageView,url,desiredWidth,desiredHeight,new OnImageListener() {
			
			@Override
			public void onSuccess(ImageView imageView, Bitmap bitmap) {
				imageView.setImageBitmap(bitmap);
			}
			
			@Override
			public void onLoading(ImageView imageView) {
			    //imageView.setImageResource(R.drawable.image_loading);
			}
			
			@Override
			public void onError(ImageView imageView) {
				
			}
			
			@Override
			public void onEmpty(ImageView imageView) {
				
			}
		});
    }
    
    public void displayBg(ImageView imageView,String url,int desiredWidth, int desiredHeight){
        download(imageView,url,desiredWidth,desiredHeight,new OnImageListener() {
            
			@Override
            public void onSuccess(ImageView imageView, Bitmap bitmap) {
                //imageView.setImageBitmap(bitmap);
                imageView.setBackground(new BitmapDrawable(bitmap));
            }
            
            @Override
            public void onLoading(ImageView imageView) {
                //imageView.setImageResource(R.drawable.image_loading);
            }
            
            @Override
            public void onError(ImageView imageView) {
                
            }
            
            @Override
            public void onEmpty(ImageView imageView) {
                
            }
        });
    }
    
    /**
     * 
     * 显示这个图片(按照原图尺寸).
     * ImageView使用android:scaleType="fitXY"属性仍旧可以充满
     * @param imageView
     * @param url
     */
    public void display(ImageView imageView,String url){
    	download(imageView,url,-1,-1,new OnImageListener() {
			
			@Override
			public void onSuccess(ImageView imageView, Bitmap bitmap) {
				imageView.setImageBitmap(bitmap);
			}
			
			@Override
			public void onLoading(ImageView imageView) {
			    //imageView.setImageResource(R.drawable.image_loading);
			}
			
			@Override
			public void onError(ImageView imageView) {
				
			}
			
			@Override
			public void onEmpty(ImageView imageView) {
				
			}
		});
    }
    
    /**
     * 
     * 显示这个图片(按照原图尺寸).
     * ImageView使用android:scaleType="fitXY"属性仍旧可以充满
     * @param imageView
     * @param url
     */
    public void displayShare(ImageView imageView,String url){
    	download(imageView,url,-1,-1,new OnImageListener() {
			
			@Override
			public void onSuccess(ImageView imageView, Bitmap bitmap) {
				imageView.setImageBitmap(bitmap);
			}
			
			@Override
			public void onLoading(ImageView imageView) {
			    imageView.setImageResource(R.drawable.image_loading);
			}
			
			@Override
			public void onError(ImageView imageView) {
				
			}
			
			@Override
			public void onEmpty(ImageView imageView) {
				
			}
		});
    }
    
    /**
     * 
     * 显示这个图片(按照原图尺寸).
     * ImageView使用android:scaleType="fitXY"属性仍旧可以充满
     * @param imageView
     * @param url
     */
    public void display(ImageView imageView,final int defaultImage,String url){
        download(imageView,url,-1,-1,new OnImageListener() {
            
            @Override
            public void onSuccess(ImageView imageView, Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
            
            @Override
            public void onLoading(ImageView imageView) {
                //imageView.setImageResource(R.drawable.image_loading);
                imageView.setImageResource(R.drawable.default_head);
                
            }
            
            @Override
            public void onError(ImageView imageView) {
                imageView.setImageResource(defaultImage);
            }
            
            @Override
            public void onEmpty(ImageView imageView) {
                imageView.setImageResource(defaultImage);
            }
        });
    }
    
    /**
     * 
     * 显示这个图片.
     * @param imageView
     * @param loadingView
     * @param url
     * @param desiredWidth
     * @param desiredHeight
     */
    public void display(final ImageView imageView,final View loadingView,final String url,final int desiredWidth,final int desiredHeight){
    	download(imageView,url,desiredWidth,desiredHeight,new OnImageListener() {
			
    		public void onSuccess(ImageView imageView, Bitmap bitmap) {
				imageView.setImageBitmap(bitmap);
				imageView.setVisibility(View.VISIBLE);
				loadingView.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoading(ImageView imageView) {
				imageView.setVisibility(View.GONE);
				loadingView.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onError(ImageView imageView) {
				
			}
			
			@Override
			public void onEmpty(ImageView imageView) {
				
			}
		});
    }
	
     
    public void displayImage(final ImageView imageView,final View loadingView,final String url,final int desiredWidth,final int desiredHeight){
        download(imageView,url,desiredWidth,desiredHeight,new OnImageListener() {
            
            public void onSuccess(ImageView imageView, Bitmap bitmap) {
                imageView.setImageBitmap(null);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
            }
            
            @Override
            public void onLoading(ImageView imageView) {
                //imageView.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
            }
            
            @Override
            public void onError(ImageView imageView) {
                imageView.setImageResource(R.drawable.image_error);
            }
            
            @Override
            public void onEmpty(ImageView imageView) {
                imageView.setImageResource(R.drawable.image_error);
            }
        });
    }
    
    
    /**
     * 
     * 下载这个图片.
     * @param imageView
     * @param url
     * @param desiredWidth
     * @param desiredHeight
     */
    public void download(final ImageView imageView,final String url,final int desiredWidth,final int desiredHeight,final OnImageListener onImageListener) { 
    	
		if(AbStrUtil.isEmpty(url)){
			if(onImageListener!=null){
				onImageListener.onEmpty(imageView);
			}
    		return;
    	}
    	
    	final String cacheKey = memCache.getCacheKey(url, desiredWidth, desiredHeight);
    	//先看内存
    	Bitmap bitmap = memCache.getBitmap(cacheKey);
    	AbLogUtil.i(AbImageLoader.class, "从LRUCache中获取到的图片"+cacheKey+":"+bitmap);
    	
    	if(bitmap != null){
    		if(onImageListener!=null){
    		    onImageListener.onSuccess(imageView, bitmap);
    		}
    	}else{
    		
    		if(onImageListener!=null){
				onImageListener.onLoading(imageView);
			}
    		
    		if(imageView != null){
	        	//设置标记,目的解决闪烁问题
	            imageView.setTag(url);
    		}
    		
    		AbTaskItem item = new AbTaskItem();
            item.setListener(new AbTaskObjectListener(){
            	
                @Override
                public <T> void update(T entity) {
                	AbBitmapResponse response = (AbBitmapResponse)entity;
                	if(response == null){
                		if(onImageListener!=null){
                			onImageListener.onError(imageView);
                    	}
                	}else{
                		Bitmap bitmap = response.getBitmap();
                		//要判断这个imageView的url有变化，如果没有变化才set
                        //有变化就取消，解决列表的重复利用View的问题
                		if(bitmap==null){
                			if(onImageListener!=null){
                        		onImageListener.onEmpty(imageView);
                        		
                        	}
                		}else if(imageView == null || url.equals(imageView.getTag())){
                    		if(onImageListener!=null){
                        		onImageListener.onSuccess(imageView, bitmap);
                        	}
                    	}
                		AbLogUtil.d(AbImageLoader.class, "获取到图片："+bitmap);
                	}
                	
                }

    			@Override
                public AbBitmapResponse getObject() {
    				return getBitmapResponse(url, desiredWidth, desiredHeight);
                }
                
            });
            
            add2Queue(item);
    	}
    	
    } 
    
    
    /**
     * 
     * 下载这个图片.
     * @param url
     * @param desiredWidth
     * @param desiredHeight
     */
    public void download(final String url,final int desiredWidth,final int desiredHeight,final OnImageListener2 onImageListener) { 
    	
		if(AbStrUtil.isEmpty(url)){
			if(onImageListener!=null){
				onImageListener.onEmpty();
			}
    		return;
    	}
    	
    	final String cacheKey = memCache.getCacheKey(url, desiredWidth, desiredHeight);
    	//先看内存
    	Bitmap bitmap = memCache.getBitmap(cacheKey);
    	AbLogUtil.i(AbImageLoader.class, "从LRUCache中获取到的图片"+cacheKey+":"+bitmap);
    	
    	if(bitmap != null){
    		if(onImageListener!=null){
    		    onImageListener.onSuccess(bitmap);
    		}
    	}else{
    		
    		if(onImageListener!=null){
				onImageListener.onLoading();
			}
    		
    		AbTaskItem item = new AbTaskItem();
            item.setListener(new AbTaskObjectListener(){
            	
                @Override
                public <T> void update(T entity) {
                	AbBitmapResponse response = (AbBitmapResponse)entity;
                	if(response == null){
                		if(onImageListener!=null){
                			onImageListener.onError();
                    	}
                	}else{
                		Bitmap bitmap = response.getBitmap();
                		//要判断这个imageView的url有变化，如果没有变化才set
                        //有变化就取消，解决列表的重复利用View的问题
                		if(bitmap==null){
                			if(onImageListener!=null){
                        		onImageListener.onEmpty();
                        	}
                		}else {
                    		if(onImageListener!=null){
                        		onImageListener.onSuccess(bitmap);
                        	}
                    	}
                		AbLogUtil.d(AbImageLoader.class, "获取到图片："+bitmap);
                	}
                	
                }

    			@Override
                public AbBitmapResponse getObject() {
                    try {
                    	return getBitmapResponse(url, desiredWidth, desiredHeight);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                
            });
            
            add2Queue(item);
    	}
    	
    } 
    
    /**
     * 
     * 获取AbBitmapResponse.
     * @param url
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    public AbBitmapResponse getBitmapResponse(String url,int desiredWidth,int desiredHeight){
    	AbBitmapResponse bitmapResponse = null;
		try {
			final String cacheKey = memCache.getCacheKey(url, desiredWidth, desiredHeight);
			Bitmap bitmap = null;
			//看磁盘
			Entry entry = diskCache.get(cacheKey);
			if(entry == null || entry.isExpired()){
				AbLogUtil.i(AbImageLoader.class, "图片磁盘中没有，或者已经过期");
				
				AbCacheResponse response = AbCacheUtil.getCacheResponse(url,expiresTime);
				if(response!=null){
					bitmap =  AbImageUtil.getBitmap(response.data, desiredWidth, desiredHeight);
					if(bitmap!=null){
						memCache.putBitmap(cacheKey, bitmap);
						diskCache.put(cacheKey, AbCacheHeaderParser.parseCacheHeaders(response));
					}
				}
			}else{
				//磁盘中有
				byte [] bitmapData = entry.data;
				bitmap =  AbImageUtil.getBitmap(bitmapData, desiredWidth, desiredHeight);
				memCache.putBitmap(cacheKey, bitmap);
			}
			
			bitmapResponse = new AbBitmapResponse(url);
			bitmapResponse.setBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return bitmapResponse;
    }
    

	/**
	 * 获取失效时间.
	 *
	 * @return the expires time
	 */
	public int getExpiresTime() {
		return expiresTime;
	}


	/**
	 * 设置失效时间.
	 *
	 * @param expiresTime the new expires time
	 */
	public void setExpiresTime(int expiresTime) {
		this.expiresTime = expiresTime;
	}
	
	/**
	 * 监听器
	 */
	public interface OnImageListener {
		
		/**
		 * On Empty.
		 * @param imageView
		 */
		public void onEmpty(ImageView imageView);
		
		/**
		 * On Loading.
		 * @param imageView
		 */
		public void onLoading(ImageView imageView);
		
		/**
		 * On Error.
		 * @param imageView
		 */
		public void onError(ImageView imageView);
		
		/**
		 * 
		 * On response.
		 * @param imageView
		 * @param bitmap
		 */
		public void onSuccess(ImageView imageView, Bitmap bitmap);
	}
	
	/**
	 * 监听器
	 */
	public interface OnImageListener2 {

		public void onEmpty();
		

		public void onLoading();
		

		public void onError();
		

		public void onSuccess(Bitmap bitmap);
	}

	/**
	 * 
	 * 增加到最少的队列中.
	 * @param item
	 */
	private void add2Queue(AbTaskItem item){
		int minQueueIndex = 0;
		if(taskQueueList.size() == 0){
			AbTaskQueue queue = AbTaskQueue.newInstance();
			taskQueueList.add(queue);
			queue.execute(item);
		}else{
			int minSize = 0;
			for(int i=0;i<taskQueueList.size();i++){
				AbTaskQueue queue = taskQueueList.get(i);
				int size = queue.getTaskItemListSize();
				if(i==0){
					minSize = size;
					minQueueIndex = i;
				}else{
					if(size < minSize){
						minSize = size;
						minQueueIndex = i;
					}
				}
			}
			if(taskQueueList.size() <5 && minSize > 2){
				AbTaskQueue queue = AbTaskQueue.newInstance();
				taskQueueList.add(queue);
				queue.execute(item);
			}else{
				AbTaskQueue minQueue = taskQueueList.get(minQueueIndex);
				minQueue.execute(item);
			}
			
		}
		
		for(int i=0;i<taskQueueList.size();i++){
			AbTaskQueue queue = taskQueueList.get(i);
			int size = queue.getTaskItemListSize();
			AbLogUtil.i(AbImageLoader.class, "线程队列["+i+"]的任务数："+size);
		}
		
	}
	
	/**
	 * 
	 * 释放资源.
	 */
	public void cancelAll(){
		for(int i=0;i<taskQueueList.size();i++){
			AbTaskQueue queue = taskQueueList.get(i);
			queue.cancel(true);
		}
		taskQueueList.clear();
	}


	public AbImageBaseCache getMemCache() {
		return memCache;
	}


	public void setMemCache(AbImageBaseCache memCache) {
		this.memCache = memCache;
	}


	public AbDiskBaseCache getDiskCache() {
		return diskCache;
	}


	public void setDiskCache(AbDiskBaseCache diskCache) {
		this.diskCache = diskCache;
	}
	
}
