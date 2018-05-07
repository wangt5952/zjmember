package com.laf.manager.core.support;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    public interface FileAllJsonView {};

    @JsonView(FileAllJsonView.class)
    private String url;
}
