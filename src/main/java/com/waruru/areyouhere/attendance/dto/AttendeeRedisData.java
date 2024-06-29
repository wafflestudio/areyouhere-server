package com.waruru.areyouhere.attendance.dto;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendeeRedisData {
    private Long id;
    private String name;

    private String note;

    @Builder
    public AttendeeRedisData(Long id, String name, String note) {
        this.id = id;
        this.name = name;
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AttendeeRedisData that = (AttendeeRedisData) o;

        if (!Objects.equals(id, that.id)) {
            return false;
        }
        if (!Objects.equals(name, that.name)) {
            return false;
        }
        return Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        return result;
    }
}
