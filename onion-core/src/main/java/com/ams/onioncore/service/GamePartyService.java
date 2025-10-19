package com.ams.onioncore.service;

import com.ams.onioncore.dto.GamePartyRequest;
import com.ams.onioncore.dto.GamePartyResponse;
import com.ams.onioncore.exception.CustomException;
import com.ams.onioncore.exception.ErrorCode;
import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.User;
import com.ams.oniondomain.entity.enums.PartyStatus;
import com.ams.oniondomain.repository.GamePartyRepository;
import com.ams.oniondomain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GamePartyService {
    private final GamePartyRepository gamePartyRepository;
    private final UserRepository userRepository;

    /** 모집글 등록 */
    public GamePartyResponse create(String email, GamePartyRequest request) {
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        GameParty party = GameParty.builder()
                .title(request.getTitle())
                .gameName(request.getGameName())
                .type(request.getType())
                .maxPlayer(request.getMaxPlayer())
                .description(request.getDescription())
                .creator(creator)
                .status(PartyStatus.OPEN)
                .build();

        return GamePartyResponse.from(gamePartyRepository.save(party));
    }

    /** 전체 목록 조회 */
    @Transactional(readOnly = true)
    public Page<GamePartyResponse> getAllPaged(Pageable pageable) {
        return gamePartyRepository.findAll(pageable)
                .map(GamePartyResponse::from);
    }

    /** 단건 조회 */
    @Transactional(readOnly = true)
    public GamePartyResponse getById(Long id) {
        GameParty party = gamePartyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));
        return GamePartyResponse.from(party);
    }

    /** 수정 */
    public GamePartyResponse update(String email, Long id, GamePartyRequest request) {
        GameParty party = gamePartyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        if (!party.getCreator().getEmail().equals(email)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        party.update(
                request.getTitle(),
                request.getGameName(),
                request.getMaxPlayer(),
                request.getDescription(),
                request.getType()
        );

        return GamePartyResponse.from(party);
    }

    /** 삭제 */
    public void delete(String email, Long id) {
        GameParty party = gamePartyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        if (!party.getCreator().getEmail().equals(email)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        gamePartyRepository.delete(party);
    }

    public GamePartyResponse changeStatus(String email, Long id, String newStatus) {
        GameParty party = gamePartyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        if (!party.getCreator().getEmail().equals(email)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        party.changeStatus(PartyStatus.valueOf(newStatus));
        return GamePartyResponse.from(party);
    }
}
