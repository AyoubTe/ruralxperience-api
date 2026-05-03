package com.ruralxperience.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruralxperience.dto.request.CreateBookingRequest;
import com.ruralxperience.dto.response.BookingResponse;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.entity.User;
import com.ruralxperience.enums.BookingStatus;
import com.ruralxperience.enums.Role;
import com.ruralxperience.security.JwtService;
import com.ruralxperience.security.config.SecurityConfig;
import com.ruralxperience.service.BookingService;
import com.ruralxperience.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@Import(SecurityConfig.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private BookingService bookingService;

    @Test
    @WithMockUser(username = "explorer@ruralxperience.com", roles = {"EXPLORER"})
    void shouldCreate() throws Exception {
        // Arrange
        Long explorerID = 1L;
        Long experienceID = 23L;
        LocalDate startDate = LocalDate.now().plusDays(12);
        LocalDate endDate = LocalDate.now().plusDays(16);
        Integer numberOfGuests = 3;
        String specialRequest = "Hello, sir we are motivated to try your experience. And we can't wait to meet you and discover the incredible beauty of your town.";

        CreateBookingRequest request = new CreateBookingRequest(experienceID, startDate, endDate, numberOfGuests, specialRequest);

        BookingResponse response = new BookingResponse(
                100L,
                experienceID,
                "Amazing Rural Getaway",
                "https://example.com/photo.jpg",
                "Normandy, France",
                explorerID,
                "Alice",
                "Wonder",
                "alice@example.com",
                startDate,
                endDate,
                numberOfGuests,
                new BigDecimal("450.00"),
                BookingStatus.PENDING,
                specialRequest,
                null,
                null,
                true,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        User mockUser = new User();
        mockUser.setId(explorerID);
        mockUser.setEmail("explorer@ruralxperience.com");
        mockUser.setRole(Role.EXPLORER);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock the service
        when(bookingService.create(any(CreateBookingRequest.class), eq(explorerID))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/bookings")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.experienceId").value(23L));
    }

    @Test
    @WithMockUser(username = "host@test.com", roles = {"HOST"})
    void shouldNotCreate() throws Exception {
        // Arrange
        Long explorerID = 1L;
        Long experienceID = 23L;
        LocalDate startDate = LocalDate.now().plusDays(12);
        LocalDate endDate = LocalDate.now().plusDays(16);
        Integer numberOfGuests = 3;
        String specialRequest = "Hello, sir we are motivated to try your experience. And we can't wait to meet you and discover the incredible beauty of your town.";

        CreateBookingRequest request = new CreateBookingRequest(experienceID, startDate, endDate, numberOfGuests, specialRequest);

        BookingResponse response = new BookingResponse(
                100L,
                experienceID,
                "Amazing Rural Getaway",
                "https://example.com/photo.jpg",
                "Normandy, France",
                explorerID,
                "Alice",
                "Wonder",
                "alice@example.com",
                startDate,
                endDate,
                numberOfGuests,
                new BigDecimal("450.00"),
                BookingStatus.PENDING,
                specialRequest,
                null,
                null,
                true,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        User mockUser = new User();
        mockUser.setId(explorerID);
        mockUser.setEmail("host@test.com");
        mockUser.setRole(Role.HOST);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock the service
        when(bookingService.create(any(CreateBookingRequest.class), eq(explorerID))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/bookings")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "explorer@test.com", roles = {"EXPLORER"})
    void shouldGetMyBookings() throws Exception {
        // Arrange
        Long explorerID = 1L;
        Long experienceID = 23L;
        LocalDate startDate = LocalDate.now().plusDays(12);
        LocalDate endDate = LocalDate.now().plusDays(16);
        Integer numberOfGuests = 3;
        String specialRequest = "Hello, sir we are motivated to try your experience. And we can't wait to meet you and discover the incredible beauty of your town.";

        BookingResponse response1 = new BookingResponse(
                100L,
                experienceID,
                "Amazing Rural Getaway",
                "https://example.com/photo.jpg",
                "Normandy, France",
                explorerID,
                "Alice",
                "Wonder",
                "alice@example.com",
                startDate,
                endDate,
                numberOfGuests,
                new BigDecimal("450.00"),
                BookingStatus.PENDING,
                specialRequest,
                null,
                null,
                true,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        BookingResponse response2 = new BookingResponse(
                101L,
                experienceID,
                "Amazing Rural Getaway",
                "https://example.com/photo.jpg",
                "Normandy, France",
                explorerID,
                "Alice",
                "Wonder",
                "alice@example.com",
                startDate,
                endDate,
                numberOfGuests,
                new BigDecimal("450.00"),
                BookingStatus.COMPLETED,
                specialRequest,
                null,
                null,
                true,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        BookingResponse response3 = new BookingResponse(
                102L,
                experienceID,
                "Amazing Rural Getaway",
                "https://example.com/photo.jpg",
                "Normandy, France",
                explorerID,
                "Alice",
                "Wonder",
                "alice@example.com",
                startDate,
                endDate,
                numberOfGuests,
                new BigDecimal("450.00"),
                BookingStatus.CANCELLED,
                specialRequest,
                null,
                null,
                true,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        User mockUser = new User();
        mockUser.setId(explorerID);
        mockUser.setEmail("explorer@ruralxperience.com");
        mockUser.setRole(Role.EXPLORER);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Pageable pageable = PageRequest.of(0, 10);

        List<BookingResponse> bookings = Arrays.asList(response1, response2, response3);

        Page<BookingResponse> bookingPage = new PageImpl<>(bookings, pageable, bookings.size());

        PageResponse<BookingResponse> bookingResponsePage = PageResponse.from(bookingPage);

        when(bookingService.getExplorerBookings(eq(explorerID), any(Pageable.class))).thenReturn(bookingResponsePage);

        // Act and Assert
        mockMvc.perform(get("/api/v1/bookings/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)));
    }

    @Test
    void hostBookings() {
    }

    @Test
    void getById() {
    }

    @Test
    void confirm() {
    }

    @Test
    void decline() {
    }

    @Test
    void cancel() {
    }
}