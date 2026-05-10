package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.enumconstant.ClaimStatus;
import college.smart_lost_found_backend.model.Claim;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClaimDaoImpl implements ClaimDao {
    private final JdbcTemplate jdbcTemplate;


    public ClaimDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Claim> claimRowMapper = (rs, rowNum) -> {
        Claim claim = new Claim();

        claim.setClaimId(rs.getLong("claim_id"));
        claim.setItemId(rs.getLong("item_id"));
        claim.setUserId(rs.getLong("user_id"));
        claim.setClaimMessage(rs.getString("claim_message"));
        claim.setStatus(ClaimStatus.valueOf(rs.getString("status")));

        if (rs.getTimestamp("claimed_at") != null) {
            claim.setClaimedAt(rs.getTimestamp("claimed_at").toLocalDateTime());
        }

        if (rs.getTimestamp("updated_at") != null) {
            claim.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        return claim;
    };

    @Override
    public int save(Claim claim) {
        String sql = """
                INSERT INTO claims (item_id, user_id, claim_message, status)
                VALUES (?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, claim.getItemId());
            ps.setLong(2, claim.getUserId());
            ps.setString(3, claim.getClaimMessage());
            ps.setString(4, claim.getStatus().name());
            return ps;

        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Optional<Claim> findById(Long claimId) {
        String sql = "SELECT * FROM claims WHERE claim_id = ?";

        List<Claim> claims = jdbcTemplate.query(sql, claimRowMapper, claimId);

        return claims.stream().findFirst();
    }

    @Override
    public List<Claim> findAll() {
        String sql = "SELECT * FROM claims ORDER BY claimed_at DESC";

        return jdbcTemplate.query(sql, claimRowMapper);
    }

    @Override
    public List<Claim> findByItemId(Long itemId) {
        String sql = "SELECT * FROM claims WHERE item_id = ? ORDER BY claimed_at DESC";

        return jdbcTemplate.query(sql, claimRowMapper, itemId);
    }

    @Override
    public List<Claim> findByUserId(Long userId) {
        String sql = "SELECT * FROM claims WHERE user_id = ? ORDER BY claimed_at DESC";

        return jdbcTemplate.query(sql, claimRowMapper, userId);
    }

    @Override
    public int updateStatus(Long claimId, ClaimStatus status) {
        String sql = "UPDATE claims SET status = ? WHERE claim_id = ?";

        return jdbcTemplate.update(sql, status.name(), claimId);
    }

    @Override
    public int rejectOtherClaims(Long itemId, Long approvedClaimId) {
        String sql = """
                UPDATE claims
                SET status = 'REJECTED'
                WHERE item_id = ?
                AND claim_id <> ?
                AND status = 'PENDING'
                """;

        return jdbcTemplate.update(sql, itemId, approvedClaimId);
    }

    @Override
    public int deleteById(Long claimId) {
        String sql = "DELETE FROM claims WHERE claim_id = ?";

        return jdbcTemplate.update(sql, claimId);
    }
}
