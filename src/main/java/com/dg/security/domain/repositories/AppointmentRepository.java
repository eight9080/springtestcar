package com.dg.security.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dg.security.domain.entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
