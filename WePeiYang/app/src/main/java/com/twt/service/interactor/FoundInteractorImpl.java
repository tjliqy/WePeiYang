package com.twt.service.interactor;

import android.util.Log;

import com.google.gson.JsonElement;
import com.twt.service.api.ApiClient;
import com.twt.service.bean.Found;
import com.twt.service.bean.FoundDetails;
import com.twt.service.bean.Upload;
import com.twt.service.ui.lostfound.found.details.FailureEvent;
import com.twt.service.ui.lostfound.found.details.SuccessEvent;
import com.twt.service.ui.lostfound.post.found.event.PostFoundFailureEvent;
import com.twt.service.ui.lostfound.post.found.event.PostFoundSuccessEvent;
import com.twt.service.ui.lostfound.post.found.event.UploadFailureEvent;
import com.twt.service.ui.lostfound.post.found.event.UploadSuccessEvent;
import com.twt.service.ui.lostfound.post.mypost.myfound.event.GetMyFoundFailureEvent;
import com.twt.service.ui.lostfound.post.mypost.myfound.event.GetMyFoundSuccessEvent;

import java.io.File;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 16/2/20.
 */
public class FoundInteractorImpl implements FoundInteractor {
    @Override
    public void getFoundList(int page) {
        ApiClient.getFoundList(page, new Callback<Found>() {
            @Override
            public void success(Found found, Response response) {
                EventBus.getDefault().post(new com.twt.service.ui.lostfound.found.SuccessEvent(found));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new com.twt.service.ui.lostfound.found.FailureEvent(error));
            }
        });
    }

    @Override
    public void getFoundDetails(int id) {
        ApiClient.getFoundDetails(id, new Callback<FoundDetails>() {
            @Override
            public void success(FoundDetails details, Response response) {
                EventBus.getDefault().post(details);
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new FailureEvent(error));
            }
        });
    }

    @Override
    public void uploadImage(File file) {
        ApiClient.uploadImage("lostfound", file, new Callback<List<Upload>>() {
            @Override
            public void success(List<Upload> uploads, Response response) {
                EventBus.getDefault().post(new UploadSuccessEvent(uploads));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new UploadFailureEvent(error));
            }
        });
    }

    @Override
    public void postEdit(String authorization, int id, String title, String name, String time, String place, String phone, String content, String found_pic) {
        if (found_pic == null) {
            Log.e("pic 空", "空");
        }
        ApiClient.editFound(authorization, id, title, name, time, phone, place, content, found_pic, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                EventBus.getDefault().post(new PostFoundSuccessEvent());
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new PostFoundFailureEvent(error));
            }
        });
    }

    @Override
    public void postFound(String authorization, String title, String name, String time, String place, String phone, String content, String found_pic) {
        ApiClient.postFound(authorization, title, name, time, place, phone, content, found_pic, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                EventBus.getDefault().post(new PostFoundSuccessEvent());
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new PostFoundFailureEvent(error));
            }
        });
    }

    @Override
    public void getMyFoundList(String authorization, int page) {
        ApiClient.getMyFoundList(authorization, page, new Callback<Found>() {
            @Override
            public void success(Found found, Response response) {
                EventBus.getDefault().post(new GetMyFoundSuccessEvent(found));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new GetMyFoundFailureEvent(error));
            }
        });
    }
}
