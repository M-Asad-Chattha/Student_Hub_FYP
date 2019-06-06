package com.achba.studenthub.ChatNotifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAkbjZLsY:APA91bFzHpmnTdiAp_mpkVhkTB0wlrVeHtHRGTZ6QiS4cO9GOLKd_WkQCPSOXgGA0UyBpFcIYwcVt2w6bVBTLDrHHP-H5RssVzb10aDvMOeAvWUMDz8rSu2LiHdAmmVhtkbmandQEyLB"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
