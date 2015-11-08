package com.stxnext.intranet2.backend.retrofit;

import com.google.gson.Gson;
import com.stxnext.intranet2.backend.model.project.ProjectResponse;
import com.stxnext.intranet2.backend.model.workedHour.WorkedHours;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by bkosarzycki on 02.11.15.
 */
public class /* interface */ ProjectListService { //todo: swap to interface when backend is ready!

    //@GET("/api/projects")
    //ProjectResponse getProjects();

    //todo: swap to retrofit when backend is ready!
    static public ProjectResponse getProjects() {
        String projects = "{projects: \n" +
                "    [\n" +
                "        {\n" +
                "            id: 1,\n" +
                "            name: \"Decernis / gComply\"  \n" +
                "         },\n" +

                "        {\n" +
                "            id: 1,\n" +
                "            name: \"Hogarth / ZADAR\"  \n" +
                "         }\n" +

                "    ]\n" +
                "}";
        try { Thread.sleep(700); } catch  (Exception exc) {}
        return new Gson().fromJson(projects , ProjectResponse.class);
    }
}

