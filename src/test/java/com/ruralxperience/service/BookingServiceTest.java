package com.ruralxperience.service;

import com.ruralxperience.dto.request.CreateBookingRequest;
import com.ruralxperience.dto.response.BookingResponse;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.entity.Booking;
import com.ruralxperience.entity.Category;
import com.ruralxperience.entity.Experience;
import com.ruralxperience.entity.HostProfile;
import com.ruralxperience.entity.Review;
import com.ruralxperience.entity.User;
import com.ruralxperience.enums.BookingStatus;
import com.ruralxperience.enums.ExperienceStatus;
import com.ruralxperience.mapper.BookingMapper;
import com.ruralxperience.repository.BookingRepository;
import com.ruralxperience.repository.ExperienceRepository;
import com.ruralxperience.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ExperienceRepository  experienceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void shouldCreate() {
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

        HostProfile host = new HostProfile();
        host.setId(1L);

        User explorer = new User();
        explorer.setId(explorerID);
        explorer.setFirstName("Alice");
        explorer.setLastName("Wonder");
        explorer.setEmail("alice@example.com");

        Category category = new Category();
        category.setId(2L);

        Experience experience = Experience.builder()
                .id(23L)
                .title("Amazing Rural Getaway")
                .shortDescription("A wonderful weekend in nature.")
                .fullDescription("Here is the full description of this amazing getaway...")
                .pricePerPerson(new java.math.BigDecimal("150.00"))
                .durationDays(4)
                .maxGuests(5)
                .location("Normandy, France")
                .latitude(49.1828)
                .longitude(-0.3706)
                .country("France")
                .status(ExperienceStatus.PUBLISHED)
                .averageRating(new java.math.BigDecimal("4.8"))
                .reviewCount(12)
                .coverPhotoUrl("https://example.com/photo.jpg")
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now())
                .publishedAt(LocalDateTime.now().minusDays(9))
                .host(host)
                .category(category)
                .build();

        Booking booking = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.PENDING)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        when(experienceRepository.findByIdWithDetails(request.experienceId())).thenReturn(Optional.of(experience));
        when(bookingRepository.findConflictingBookings(experienceID, startDate, endDate)).thenReturn(List.of());
        when(userRepository.findById(explorerID)).thenReturn(Optional.of(explorer));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(bookingMapper.toResponse(booking)).thenReturn(response);

        // Act
        BookingResponse result = bookingService.create(request, explorerID);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(response, result);
    }

    @Test
    void shouldGetExplorerBookings() {
        // Arrange
        Long explorerID = 1L;
        LocalDate startDate = LocalDate.now().plusDays(12);
        LocalDate endDate = LocalDate.now().plusDays(16);
        Integer numberOfGuests = 3;
        String specialRequest = "Hello, sir we are motivated to try your experience. And we can't wait to meet you and discover the incredible beauty of your town.";

        HostProfile host = new HostProfile();
        host.setId(1L);

        User explorer = new User();
        explorer.setId(explorerID);
        explorer.setFirstName("Alice");
        explorer.setLastName("Wonder");
        explorer.setEmail("alice@example.com");

        Category category = new Category();
        category.setId(2L);

        Experience experience = Experience.builder()
                .id(23L)
                .title("Amazing Rural Getaway")
                .shortDescription("A wonderful weekend in nature.")
                .fullDescription("Here is the full description of this amazing getaway...")
                .pricePerPerson(new java.math.BigDecimal("150.00"))
                .durationDays(4)
                .maxGuests(5)
                .location("Normandy, France")
                .latitude(49.1828)
                .longitude(-0.3706)
                .country("France")
                .status(ExperienceStatus.PUBLISHED)
                .averageRating(new java.math.BigDecimal("4.8"))
                .reviewCount(12)
                .coverPhotoUrl("https://example.com/photo.jpg")
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now())
                .publishedAt(LocalDateTime.now().minusDays(9))
                .host(host)
                .category(category)
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Booking booking1 = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.PENDING)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        Booking booking2 = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.PENDING)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        Booking booking3 = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.PENDING)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        List<Booking> bookings = Arrays.asList(booking1, booking2, booking3);

        Page<Booking> mockPage = new PageImpl(bookings);

        when(bookingRepository.findByExplorerIdWithDetails(explorerID, pageable)).thenReturn(mockPage);

        // Act
        PageResponse<BookingResponse> results = bookingService.getExplorerBookings(explorerID, pageable);

        // Assert
        Assertions.assertNotNull(results);
        Assertions.assertEquals(3, results.size());
    }

    @Test
    void shouldGetHostBookings() {
        // Arrange
        Long explorerID = 1L;
        Long hostId = 1L;
        LocalDate startDate = LocalDate.now().plusDays(12);
        LocalDate endDate = LocalDate.now().plusDays(16);
        Integer numberOfGuests = 3;
        String specialRequest = "Hello, sir we are motivated to try your experience. And we can't wait to meet you and discover the incredible beauty of your town.";

        HostProfile host = new HostProfile();
        host.setId(hostId);

        User explorer = new User();
        explorer.setId(explorerID);
        explorer.setFirstName("Alice");
        explorer.setLastName("Wonder");
        explorer.setEmail("alice@example.com");

        Category category = new Category();
        category.setId(2L);

        Experience experience = Experience.builder()
                .id(23L)
                .title("Amazing Rural Getaway")
                .shortDescription("A wonderful weekend in nature.")
                .fullDescription("Here is the full description of this amazing getaway...")
                .pricePerPerson(new java.math.BigDecimal("150.00"))
                .durationDays(4)
                .maxGuests(5)
                .location("Normandy, France")
                .latitude(49.1828)
                .longitude(-0.3706)
                .country("France")
                .status(ExperienceStatus.PUBLISHED)
                .averageRating(new java.math.BigDecimal("4.8"))
                .reviewCount(12)
                .coverPhotoUrl("https://example.com/photo.jpg")
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now())
                .publishedAt(LocalDateTime.now().minusDays(9))
                .host(host)
                .category(category)
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Booking booking1 = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.PENDING)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        Booking booking2 = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.PENDING)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        Booking booking3 = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.PENDING)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        List<Booking> bookings = Arrays.asList(booking1, booking2, booking3);

        Page<Booking> mockPage = new PageImpl<>(bookings);

        when(bookingRepository.findByHostIdWithDetails(hostId, pageable)).thenReturn(mockPage);

        // Act
        Page<Booking> results = bookingRepository.findByHostIdWithDetails(hostId, pageable);

        // Assert
        Assertions.assertNotNull(results);
        Assertions.assertEquals(bookings, results.getContent());
    }

    @Test
    void shouldGetById() {
        // Arrange
        Long bookingId = 100L;
        Long userId = 1L;
        LocalDate startDate = LocalDate.now().plusDays(12);
        LocalDate endDate = LocalDate.now().plusDays(16);
        Integer numberOfGuests = 3;
        String specialRequest = "Hello, sir we are motivated to try your experience. And we can't wait to meet you and discover the incredible beauty of your town.";

        // 1. Setup Host User and Profile
        User hostUser = new User();
        hostUser.setId(99L);

        HostProfile host = new HostProfile();
        host.setId(1L);
        host.setUser(hostUser);

        // 2. Setup Explorer
        User explorer = new User();
        explorer.setId(1L);
        explorer.setFirstName("Alice");
        explorer.setLastName("Wonder");
        explorer.setEmail("alice@example.com");

        // 3. Setup Category
        Category category = new Category();
        category.setId(2L);

        // 4. Build Experience
        Experience experience = Experience.builder()
                .id(23L)
                .title("Amazing Rural Getaway")
                .shortDescription("A wonderful weekend in nature.")
                .fullDescription("Here is the full description of this amazing getaway...")
                .pricePerPerson(new java.math.BigDecimal("150.00"))
                .durationDays(4)
                .maxGuests(5)
                .location("Normandy, France")
                .latitude(49.1828)
                .longitude(-0.3706)
                .country("France")
                .status(ExperienceStatus.PUBLISHED)
                .averageRating(new java.math.BigDecimal("4.8"))
                .reviewCount(12)
                .coverPhotoUrl("https://example.com/photo.jpg")
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now())
                .publishedAt(LocalDateTime.now().minusDays(9))
                .host(host)
                .category(category)
                .build();

        // 5. Build Booking
        Booking booking = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.PENDING)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        // 6. Build Expected Response
        BookingResponse response = new BookingResponse(
                100L,
                23L,
                "Amazing Rural Getaway",
                "https://example.com/photo.jpg",
                "Normandy, France",
                1L,
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

        when(bookingRepository.findByIdWithAllDetails(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.toResponse(booking)).thenReturn(response);

        // Act
        BookingResponse result = bookingService.getById(bookingId, userId);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(response, result);
        Assertions.assertEquals(100L, result.id());
    }

    @Test
    void shouldConfirm() {
        // Arrange
        Long bookingID = 100L;
        Long hostUserID = 1L;
        String hostMessage = "You are welcome on my Guest House!";

        LocalDate startDate = LocalDate.now().plusDays(12);
        LocalDate endDate = LocalDate.now().plusDays(16);
        Integer numberOfGuests = 3;
        String specialRequest = "Hello, sir we are motivated to try your experience. And we can't wait to meet you and discover the incredible beauty of your town.";

        // 1. Setup Host User and Profile
        User hostUser = new User();
        hostUser.setId(1L);

        HostProfile host = new HostProfile();
        host.setId(1L);
        host.setUser(hostUser);

        // 2. Setup Explorer
        User explorer = new User();
        explorer.setId(1L);
        explorer.setFirstName("Alice");
        explorer.setLastName("Wonder");
        explorer.setEmail("alice@example.com");

        // 3. Setup Category
        Category category = new Category();
        category.setId(2L);

        // 4. Build Experience
        Experience experience = Experience.builder()
                .id(23L)
                .title("Amazing Rural Getaway")
                .shortDescription("A wonderful weekend in nature.")
                .fullDescription("Here is the full description of this amazing getaway...")
                .pricePerPerson(new java.math.BigDecimal("150.00"))
                .durationDays(4)
                .maxGuests(5)
                .location("Normandy, France")
                .latitude(49.1828)
                .longitude(-0.3706)
                .country("France")
                .status(ExperienceStatus.PUBLISHED)
                .averageRating(new java.math.BigDecimal("4.8"))
                .reviewCount(12)
                .coverPhotoUrl("https://example.com/photo.jpg")
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now())
                .publishedAt(LocalDateTime.now().minusDays(9))
                .host(host)
                .category(category)
                .build();

        // 5. Build Booking
        Booking booking = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.PENDING)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        BookingResponse response = new BookingResponse(
                100L,
                23L,
                "Amazing Rural Getaway",
                "https://example.com/photo.jpg",
                "Normandy, France",
                1L,
                "Alice",
                "Wonder",
                "alice@example.com",
                startDate,
                endDate,
                numberOfGuests,
                new BigDecimal("450.00"),
                BookingStatus.CONFIRMED,
                specialRequest,
                null,
                null,
                true,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(bookingRepository.findById(bookingID)).thenReturn(Optional.of(booking));
        when(bookingRepository.findByIdWithAllDetails(bookingID)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toResponse(booking)).thenReturn(response);

        // Act
        BookingResponse result = bookingService.confirm(bookingID, hostUserID, hostMessage);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(response, result);
    }

    @Test
    void shouldDecline() {
        // Arrange
        Long bookingID = 100L;
        Long hostUserID = 1L;
        String hostMessage = "Sorry to cancel your request. But I can ensure the service for you this time.!";

        LocalDate startDate = LocalDate.now().plusDays(12);
        LocalDate endDate = LocalDate.now().plusDays(16);
        Integer numberOfGuests = 3;
        String specialRequest = "Hello, sir we are motivated to try your experience. And we can't wait to meet you and discover the incredible beauty of your town.";

        // 1. Setup Host User and Profile
        User hostUser = new User();
        hostUser.setId(1L);

        HostProfile host = new HostProfile();
        host.setId(1L);
        host.setUser(hostUser);

        // 2. Setup Explorer
        User explorer = new User();
        explorer.setId(1L);
        explorer.setFirstName("Alice");
        explorer.setLastName("Wonder");
        explorer.setEmail("alice@example.com");

        // 3. Setup Category
        Category category = new Category();
        category.setId(2L);

        // 4. Build Experience
        Experience experience = Experience.builder()
                .id(23L)
                .title("Amazing Rural Getaway")
                .shortDescription("A wonderful weekend in nature.")
                .fullDescription("Here is the full description of this amazing getaway...")
                .pricePerPerson(new java.math.BigDecimal("150.00"))
                .durationDays(4)
                .maxGuests(5)
                .location("Normandy, France")
                .latitude(49.1828)
                .longitude(-0.3706)
                .country("France")
                .status(ExperienceStatus.PUBLISHED)
                .averageRating(new java.math.BigDecimal("4.8"))
                .reviewCount(12)
                .coverPhotoUrl("https://example.com/photo.jpg")
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now())
                .publishedAt(LocalDateTime.now().minusDays(9))
                .host(host)
                .category(category)
                .build();

        // 5. Build Booking
        Booking booking = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.PENDING)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        BookingResponse response = new BookingResponse(
                100L,
                23L,
                "Amazing Rural Getaway",
                "https://example.com/photo.jpg",
                "Normandy, France",
                1L,
                "Alice",
                "Wonder",
                "alice@example.com",
                startDate,
                endDate,
                numberOfGuests,
                new BigDecimal("450.00"),
                BookingStatus.DECLINED,
                specialRequest,
                null,
                null,
                true,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(bookingRepository.findById(bookingID)).thenReturn(Optional.of(booking));
        when(bookingRepository.findByIdWithAllDetails(bookingID)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toResponse(booking)).thenReturn(response);

        // Act
        BookingResponse result = bookingService.decline(bookingID, hostUserID, hostMessage);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(response, result);
    }

    @Test
    void shouldCancel() {
        // Arrange
        Long bookingID = 100L;
        Long hostUserID = 1L;
        Long explorerId = 1L;
        String hostMessage = "Sorry to cancel your booking at that moment I no longer can ensure the service.";

        LocalDate startDate = LocalDate.now().plusDays(12);
        LocalDate endDate = LocalDate.now().plusDays(16);
        Integer numberOfGuests = 3;
        String specialRequest = "Hello, sir we are motivated to try your experience. And we can't wait to meet you and discover the incredible beauty of your town.";

        // 1. Setup Host User and Profile
        User hostUser = new User();
        hostUser.setId(hostUserID);

        HostProfile host = new HostProfile();
        host.setId(1L);
        host.setUser(hostUser);

        // 2. Setup Explorer
        User explorer = new User();
        explorer.setId(explorerId);
        explorer.setFirstName("Alice");
        explorer.setLastName("Wonder");
        explorer.setEmail("alice@example.com");

        // 3. Setup Category
        Category category = new Category();
        category.setId(2L);

        // 4. Build Experience
        Experience experience = Experience.builder()
                .id(23L)
                .title("Amazing Rural Getaway")
                .shortDescription("A wonderful weekend in nature.")
                .fullDescription("Here is the full description of this amazing getaway...")
                .pricePerPerson(new java.math.BigDecimal("150.00"))
                .durationDays(4)
                .maxGuests(5)
                .location("Normandy, France")
                .latitude(49.1828)
                .longitude(-0.3706)
                .country("France")
                .status(ExperienceStatus.PUBLISHED)
                .averageRating(new java.math.BigDecimal("4.8"))
                .reviewCount(12)
                .coverPhotoUrl("https://example.com/photo.jpg")
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now())
                .publishedAt(LocalDateTime.now().minusDays(9))
                .host(host)
                .category(category)
                .build();

        // 5. Build Booking
        Booking booking = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.PENDING)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        BookingResponse response = new BookingResponse(
                100L,
                23L,
                "Amazing Rural Getaway",
                "https://example.com/photo.jpg",
                "Normandy, France",
                explorerId,
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

        when(bookingRepository.findByIdAndExplorerId(bookingID, explorerId)).thenReturn(Optional.of(booking));
        when(bookingRepository.findById(bookingID)).thenReturn(Optional.of(booking));
        when(bookingRepository.findByIdWithAllDetails(bookingID)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toResponse(booking)).thenReturn(response);

        // Act
        BookingResponse result = bookingService.cancel(bookingID, hostUserID, hostMessage);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(response, result);
    }

    @Test
    void shouldComplete() {
        // Arrange
        Long bookingID = 100L;
        Long hostUserID = 1L;
        Long explorerId = 1L;

        LocalDate startDate = LocalDate.now().plusDays(12);
        LocalDate endDate = LocalDate.now().plusDays(16);
        Integer numberOfGuests = 3;
        String specialRequest = "Hello, sir we are motivated to try your experience. And we can't wait to meet you and discover the incredible beauty of your town.";

        // 1. Setup Host User and Profile
        User hostUser = new User();
        hostUser.setId(hostUserID);

        HostProfile host = new HostProfile();
        host.setId(1L);
        host.setUser(hostUser);

        // 2. Setup Explorer
        User explorer = new User();
        explorer.setId(explorerId);
        explorer.setFirstName("Alice");
        explorer.setLastName("Wonder");
        explorer.setEmail("alice@example.com");

        // 3. Setup Category
        Category category = new Category();
        category.setId(2L);

        // 4. Build Experience
        Experience experience = Experience.builder()
                .id(23L)
                .title("Amazing Rural Getaway")
                .shortDescription("A wonderful weekend in nature.")
                .fullDescription("Here is the full description of this amazing getaway...")
                .pricePerPerson(new java.math.BigDecimal("150.00"))
                .durationDays(4)
                .maxGuests(5)
                .location("Normandy, France")
                .latitude(49.1828)
                .longitude(-0.3706)
                .country("France")
                .status(ExperienceStatus.PUBLISHED)
                .averageRating(new java.math.BigDecimal("4.8"))
                .reviewCount(12)
                .coverPhotoUrl("https://example.com/photo.jpg")
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now())
                .publishedAt(LocalDateTime.now().minusDays(9))
                .host(host)
                .category(category)
                .build();

        // 5. Build Booking
        Booking booking = Booking.builder()
                .id(100L)
                .explorer(explorer)
                .experience(experience)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfGuests(numberOfGuests)
                .totalPrice(new BigDecimal("450.00"))
                .status(BookingStatus.CONFIRMED)
                .specialRequests(specialRequest)
                .cancellationReason(null)
                .hostMessage(null)
                .createdAt(LocalDateTime.now().minusDays(12))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .confirmedAt(LocalDateTime.now())
                .completedAt(null)
                .review(new Review())
                .build();

        BookingResponse response = new BookingResponse(
                100L,
                23L,
                "Amazing Rural Getaway",
                "https://example.com/photo.jpg",
                "Normandy, France",
                explorerId,
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

        when(bookingRepository.findByIdAndExplorerId(bookingID, explorerId)).thenReturn(Optional.of(booking));
        when(bookingRepository.findById(bookingID)).thenReturn(Optional.of(booking));
        when(bookingRepository.findByIdWithAllDetails(bookingID)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toResponse(booking)).thenReturn(response);

        // Act
        BookingResponse result = bookingService.complete(bookingID);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(response, result);
    }
}