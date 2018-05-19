package com.example.vcare.vcare.dao;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PepoManZ on 2/15/2018.
 */

public class WarningData {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("response")
        @Expose
        private List<Response> response = null;
        @SerializedName("error")
        @Expose
        private Object error;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<Response> getResponse() {
            return response;
        }

        public void setResponse(List<Response> response) {
            this.response = response;
        }

        public Object getError() {
            return error;
        }

        public void setError(Object error) {
            this.error = error;
        }
}
