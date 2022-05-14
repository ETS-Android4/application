package com.gttime.android.util;

import com.gttime.android.component.Course;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class JSONUtil {
    public static List<Course> fetchCourse(String json) {
        List<Course> list = new ArrayList<Course>();

        if (json.isEmpty()) return list;

        try {
            String result = (String) json;
            JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonResponse;

            try {
                jsonResponse = jsonObject.getJSONArray("courses");
            } catch (Exception e) {
                return new ArrayList<Course>();
            }

            int index = 0;
            int courseTerm;
            String courseTitle;
            String courseTime;
            String courseDay;
            String courseLocation;
            String courseInstructor;
            String courseCRN;
            String courseCredit;
            String courseMajor;
            String courseArea;
            String courseSection;
            String courseClass;
            String courseUniversity;
            String courseAttribute;
            while(index < jsonResponse.length()) {
                JSONObject object = jsonResponse.getJSONObject(index);
                courseTerm = object.getInt("courseTerm");
                courseTitle = object.getString("courseTitle");
                courseTime = object.getString("courseTime");
                courseDay = object.getString("courseDay");
                courseLocation = object.getString("courseLocation");
                courseInstructor = object.getString("courseInstructor");
                courseCRN = object.getString("courseCRN");
                courseCredit = object.getString("courseCredit");
                courseMajor = object.getString("courseMajor");
                courseArea = object.getString("courseArea");
                courseSection = object.getString("courseSection");
                courseClass = object.getString("courseClass");
                courseUniversity = object.getString("courseUniversity");
                courseAttribute = object.getString("courseAttribute");
                list.add(new Course(courseTerm, courseDay, courseMajor, courseTitle, courseCRN, courseArea, courseSection, courseClass, courseTime, courseLocation, courseInstructor, courseUniversity, courseCredit, courseAttribute));
                ++index;
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static String readJson(File file) {
        try {
            file.createNewFile(); // if file already exists will do nothing
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            String response = stringBuilder.toString();
            return response;
        }
        catch(IOException io) {
            io.printStackTrace();
        }

        return "";
    }

    public static void clearJson(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("");
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean appendCourse(File file, Course course) {
        try {
            String json = readJson(file);
            String response = (String) json;

            // HACK
            JSONObject jsonFile;
            if (response.isEmpty()) jsonFile = new JSONObject();
            else {
                jsonFile = new JSONObject(response);
            }

            JSONArray jsonArray;
            try {
                jsonArray = jsonFile.getJSONArray("courses");
            } catch (Exception e) {
                jsonArray = new JSONArray();
                JSONObject obj = new JSONObject();
                obj.put("courses", jsonArray);
                // TODO: chnnge it to received filename
                clearJson(file);
            }

            JSONObject jsonObject = new JSONObject();

            int courseTerm = course.getCourseTerm();
            String courseTitle = course.getCourseTitle();
            String courseTime = course.getCourseTime();
            String courseDay = course.getCourseDay();
            String courseLocation = course.getCourseLocation();
            String courseInstructor = course.getCourseInstructor();
            String courseCRN = course.getCourseCRN();
            String courseCredit = course.getCourseCredit();
            String courseMajor = course.getCourseMajor();
            String courseArea = course.getCourseArea();
            String courseSection = course.getCourseSection();
            String courseClass = course.getCourseClass();
            String courseUniversity = course.getCourseUniversity();
            String courseAttribute = course.getCourseAttribute();

            jsonObject.put("courseTerm", courseTerm);
            jsonObject.put("courseTitle", courseTitle);
            jsonObject.put("courseTime", courseTime);
            jsonObject.put("courseDay", courseDay);
            jsonObject.put("courseLocation", courseLocation);
            jsonObject.put("courseInstructor", courseInstructor);
            jsonObject.put("courseCRN", courseCRN);
            jsonObject.put("courseCredit", courseCredit);
            jsonObject.put("courseMajor", courseMajor);
            jsonObject.put("courseArea", courseArea);
            jsonObject.put("courseSection", courseSection);
            jsonObject.put("courseClass", courseClass);
            jsonObject.put("courseUniversity", courseUniversity);
            jsonObject.put("courseAttribute", courseAttribute);

            jsonArray.put(jsonObject);

            JSONObject updatedJson = new JSONObject();
            updatedJson.put("courses", jsonArray);

            String jsonString = updatedJson.toString();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonString);
            bufferedWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean deleteCourse(File file, Course course) {
        try {
            String response = readJson(file);

            // HACK
            JSONObject jsonFile;
            if (response.isEmpty()) jsonFile = new JSONObject();
            else {
                jsonFile = new JSONObject(response);
            }

            JSONArray jsonArray;
            try {
                jsonArray = jsonFile.getJSONArray("courses");
            } catch (Exception e) {
                jsonArray = new JSONArray();
                JSONObject obj = new JSONObject();
                obj.put("courses", jsonArray);
                clearJson(file);
            }

            // TODO : remove course from json

            int courseTerm = course.getCourseTerm();
            String courseTitle = course.getCourseTitle();
            String courseTime = course.getCourseTime();
            String courseDay = course.getCourseDay();
            String courseLocation = course.getCourseLocation();
            String courseInstructor = course.getCourseInstructor();
            String courseCRN = course.getCourseCRN();
            String courseCredit = course.getCourseCredit();
            String courseMajor = course.getCourseMajor();
            String courseArea = course.getCourseArea();
            String courseSection = course.getCourseSection();
            String courseClass = course.getCourseClass();
            String courseUniversity = course.getCourseUniversity();
            String courseAttribute = course.getCourseAttribute();

            JSONArray updatedJsonArray = new JSONArray();
            for (int i =0; i<jsonArray.length(); i++) {
                String elem = jsonArray.getString(i);

                if (elem.contains(courseCRN)) continue;
                updatedJsonArray.put(jsonArray.get(i));
            }

            JSONObject updatedJson = new JSONObject();
            updatedJson.put("courses", updatedJsonArray);

            String jsonString = updatedJson.toString();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonString);
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
