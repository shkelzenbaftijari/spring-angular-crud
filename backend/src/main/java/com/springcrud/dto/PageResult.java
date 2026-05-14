// ════════════════════════════════════════════════════════
// · PAGE RESULT · Generic pagination wrapper — data + pagination metadata
// ════════════════════════════════════════════════════════
package com.springcrud.dto;

import java.util.List;

public record PageResult<T>(List<T> data, int totalPages, int currentPage, long totalCount) {}
