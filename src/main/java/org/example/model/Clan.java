package org.example.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Clan {
    @JsonAlias("id")
    private long id;
    @JsonAlias("name")
    private String name;
    @JsonAlias("gold")
    private int gold;
}
