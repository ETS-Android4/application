package com.gttime.android.request;

import com.gttime.android.net.HttpConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class Request {

    public Request()  {
        throw new UnsupportedOperationException();
    };

    public static int[] queryTerm() throws ConnectException {
        try {
            String url = "http://ec2-3-238-0-205.compute-1.amazonaws.com/CourseTerm.php";
            String result = HttpConnection.read(url);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("response");

            int courseTerm;

            int count = 0;
            int arr[] = new int[jsonArray.length()];

            while(count < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(count);
                courseTerm = object.getInt("courseTerm");
                arr[count] = courseTerm;
                count++;
            }

            return arr;
        }
        catch(Exception e) {
            throw new ConnectException(e.toString());
        }
    }

    public static String[] queryMajor(String University, String term) throws ConnectException {
        try {
            String url = "http://ec2-3-238-0-205.compute-1.amazonaws.com/CourseMajor.php?courseUniversity="+ URLEncoder.encode(University,"UTF-8")
                    +"&courseTerm="+URLEncoder.encode(term,"UTF-8");
            String result = HttpConnection.read(url);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("response");

            String courseMajor;

            int count = 0;
            String arr[] = new String[jsonArray.length()];

            while(count < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(count);
                courseMajor = object.getString("courseMajor");
                arr[count] = courseMajor;
                count++;
            }

            return arr;
        }
        catch(Exception e) {
            throw new ConnectException(e.toString());
        }
    }

    public static String[] queryArea(String University, String term, String area) throws ConnectException {
        try {
            String url = "http://ec2-3-238-0-205.compute-1.amazonaws.com/CourseArea.php?courseUniversity="+ URLEncoder.encode(University,"UTF-8")
                    +"&courseTerm="+URLEncoder.encode(term,"UTF-8")
                    +"&courseMajor="+URLEncoder.encode(area,"UTF-8");
            String result = HttpConnection.read(url);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("response");

            String courseArea;

            int count = 0;
            String arr[] = new String[jsonArray.length()];

            while(count < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(count);
                courseArea = object.getString("courseArea");
                arr[count] = courseArea;
                count++;
            }

            return arr;
        }
        catch(Exception e) {
            throw new ConnectException(e.toString());
        }
    }

    public static <T> T[] ExecuteQuery(Callable<T[]> task) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<T[]> future = service.submit(task);

        try {
            return future.get();
        } catch (ExecutionException e) {
            throw e;
        } catch (InterruptedException e) {
            throw e;
        }
    }

}
