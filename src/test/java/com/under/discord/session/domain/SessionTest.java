package com.under.discord.session.domain;

import net.dv8tion.jda.core.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SessionTest {

    Session session;
    
    @Mock
    User user;
    
    @Test
    public void should_compute_spentTime_when_stop_session() throws InterruptedException {
        LocalDate startDate = LocalDate.now();
        given( user.getName() ).willReturn( "john" );
        session = new Session(startDate, new SessionTimer());

        session.declarePresence(user);
        Thread.sleep(2000L);
        session.stop();

        assertThat( session.getSessionTimeFor("john") ).isEqualTo(2L);
    }

    @Test
    public void should_compute_spentTime_when_userLeave_and_stop_session() throws InterruptedException {
        LocalDate startDate = LocalDate.now();
        User smith = mock(User.class);
        given( user.getName() ).willReturn( "john" );
        given( smith.getName() ).willReturn( "smith" );
        session = new Session(startDate, new SessionTimer());

        session.declarePresence(user);
        session.declarePresence(smith);
        Thread.sleep(2000L);
        session.declareLeave(smith);
        session.stop();

        assertThat( session.getSessionTimeFor("john") ).isEqualTo(2);
        assertThat( session.getSessionTimeFor("smith") ).isEqualTo(2);
    }
}