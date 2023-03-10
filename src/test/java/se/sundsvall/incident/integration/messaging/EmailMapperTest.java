package se.sundsvall.incident.integration.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.spring5.SpringTemplateEngine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static se.sundsvall.incident.TestDataFactory.buildAttachmentDto;
import static se.sundsvall.incident.TestDataFactory.buildAttachmentEntityList;
import static se.sundsvall.incident.TestDataFactory.buildIncidentDto;

@ExtendWith(MockitoExtension.class)
class EmailMapperTest {
    @Mock
    EmailMapperProperties properties;
    @Mock
    SpringTemplateEngine thymeleafTemplateEngine;

    EmailMapper emailMapper;

    @BeforeEach
    void setup() {
        emailMapper = new EmailMapper(properties, thymeleafTemplateEngine);
    }

    @Test
    void toMSVAEmailDto() {
        var dto = buildIncidentDto();
        when(properties.getMsvaEmailSubject()).thenReturn("someSubject");
        when(properties.getSenderName()).thenReturn("someEmailName");
        when(properties.getSenderEmailAddress()).thenReturn("someemail@host.se");
        when(properties.getMsvaRecipientEmailAddress()).thenReturn("someemail@msva.se");
        var response = emailMapper.toMSVAEmailDto(dto);
        assertThat(response).isNotNull();
        assertThat(response.getSubject()).isEqualTo("someSubject asdas - asdasd");
        assertThat(response.getEmailAddress()).isEqualTo("someemail@msva.se");
        assertThat(response.getSender().getName()).isEqualTo("someEmailName");
        assertThat(response.getSender().getAddress()).isEqualTo("someemail@host.se");
    }

    @Test
    void toEmailDto() {
        var dto = buildIncidentDto();
        var attachmentlist = buildAttachmentEntityList(2);
        when(properties.getSenderName()).thenReturn("someEmailName");
        when(properties.getSenderEmailAddress()).thenReturn("someemail@host.se");
        when(properties.getRecipientEmailAddress()).thenReturn("someemail@recipent.se");
        var response = emailMapper.toEmailDto(dto);

        assertThat(response).isNotNull();
        assertThat(response.getEmailAddress()).isEqualTo("someemail@recipent.se");
        assertThat(response.getSender().getName()).isEqualTo("someEmailName");
        assertThat(response.getSender().getAddress()).isEqualTo("someemail@host.se");
        assertThat(response.getAttachments().size()).isEqualTo(2);
    }

    @Test
    void toAttachmentEmailDto() {
        var dto = buildAttachmentDto();
        var response = emailMapper.toAttachmentEmailDto(dto);
        assertThat(response.getContent()).isEqualTo(dto.getFile());
        assertThat(response.getName()).isEqualTo(dto.getName());
        assertThat(response.getContentType()).isEqualTo(dto.getMimeType());

    }
}