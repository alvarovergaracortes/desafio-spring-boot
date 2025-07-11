package cl.previred.common.helper;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;


public class ErrorMessage{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int status;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String path;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    public ErrorMessage(String message, int status, String path) {this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }

}
