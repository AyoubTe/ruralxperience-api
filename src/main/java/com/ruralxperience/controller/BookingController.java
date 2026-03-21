package com.ruralxperience.controller;

import com.ruralxperience.dto.request.CreateBookingRequest;
import com.ruralxperience.dto.response.BookingResponse;
import com.ruralxperience.dto.response.PageResponse;
import com.ruralxperience.entity.User;
import com.ruralxperience.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@Valid @RequestBody CreateBookingRequest request,
                                  @AuthenticationPrincipal User user) {
        return bookingService.create(request, user.getId());
    }

    @GetMapping("/my")
    public PageResponse<BookingResponse> myBookings(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return bookingService.getExplorerBookings(user.getId(), pageable);
    }

    @GetMapping("/host")
    public PageResponse<BookingResponse> hostBookings(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return bookingService.getHostBookings(user.getId(), pageable);
    }

    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return bookingService.getById(id, user.getId());
    }

    @PostMapping("/{id}/confirm")
    public BookingResponse confirm(@PathVariable Long id,
                                   @AuthenticationPrincipal User user,
                                   @RequestBody(required = false) Map<String, String> body) {
        String message = body != null ? body.get("message") : null;
        return bookingService.confirm(id, user.getId(), message);
    }

    @PostMapping("/{id}/decline")
    public BookingResponse decline(@PathVariable Long id,
                                   @AuthenticationPrincipal User user,
                                   @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        return bookingService.decline(id, user.getId(), reason);
    }

    @PostMapping("/{id}/cancel")
    public BookingResponse cancel(@PathVariable Long id,
                                  @AuthenticationPrincipal User user,
                                  @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        return bookingService.cancel(id, user.getId(), reason);
    }
}
