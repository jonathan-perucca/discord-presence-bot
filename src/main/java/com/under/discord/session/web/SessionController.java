package com.under.discord.session.web;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.session.mapper.SessionMapper;
import com.under.discord.session.mapper.SessionRecordDTOs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionComponent sessionComponent;
    private final SessionMapper sessionMapper;

    @Autowired
    public SessionController(SessionComponent sessionComponent,
                             SessionMapper sessionMapper) {
        this.sessionComponent = sessionComponent;
        this.sessionMapper = sessionMapper;
    }

    @PutMapping
    public HttpResponse switchSessionState(@RequestBody @Valid SessionCommand sessionCommand) {
        if (sessionCommand.isStartCommand()) {
            sessionComponent.startSession();
        }
        if (sessionCommand.isStopCommand()) {
            sessionComponent.stopSession();
        }

        return new HttpResponse("session " + sessionCommand.getCommand());
    }

    @GetMapping("/current")
    public SessionRecordDTOs getCurrentSessionRecords() {
        List<SessionRecord> sessionRecords = sessionComponent.getCurrentSessionRecords();

        return sessionMapper.toSessionRecordDTOs(sessionRecords);
    }
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class SessionCommand {
    @NotNull
    private String command;

    public boolean isStartCommand() {
        return "start".equalsIgnoreCase(command);
    }

    public boolean isStopCommand() {
        return "stop".equalsIgnoreCase(command);
    }
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class HttpResponse {
    private String message;
}