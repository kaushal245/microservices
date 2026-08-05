package com.raise_ticket_service.reposatory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raise_ticket_service.entities.RaiseSupportTicketEntity;


@Repository
public interface RaiseSupportTicketRepository extends JpaRepository<RaiseSupportTicketEntity, Integer> {

}
