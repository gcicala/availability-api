/**
 * 
 */
package com.tui.proof.ws.security.repository;

import static com.tui.proof.ws.utils.JsonUtil.loadJSON;
import static com.tui.proof.ws.utils.JsonUtil.writeJSON;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.tui.proof.ws.security.entity.JwtTokenBookingHolder;
import com.tui.proof.ws.security.entity.JwtTokenBookingHolderRole;
import com.tui.proof.ws.security.exception.JwtException;
import com.tui.proof.ws.security.model.repository.JwtTokenRepository;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 13.05.09
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.JwtTokenRepository
 * 
 */
@Component
public class JwtTokenBookingHolderRepository implements JwtTokenRepository<JwtTokenBookingHolder> {

	@Value("${security.jwt.token.repository.folder}")
	private Path repositoryPath;

	@Async
	private final void write(
			List<JwtTokenBookingHolder> holders) throws JwtException {
		try {
			writeJSON(repositoryPath.resolve(repositoryPath.toAbsolutePath()).toFile(), getRepositoryClass().getSimpleName() + ".json", holders);
		} catch (Exception e) {
			throw new JwtException(e.getMessage(), "WRITE_REPOSITORY_ERROR");
		}
	}

	@Override
	public Class<JwtTokenBookingHolder> getRepositoryClass() {
		return JwtTokenBookingHolder.class;
	}

	@Override
	public final JwtTokenBookingHolder findByUsername(
			String username) throws JwtException {
		return findAll().stream().filter(jwtToken -> jwtToken.getUsername().equals(username)).findFirst().orElseThrow(() -> new JwtException("No " + getRepositoryClass().getSimpleName() + " with id " + username, "INVALID_USERNAME"));
	}

	@Override
	public final List<JwtTokenBookingHolder> findAll() throws JwtException {
		List<JwtTokenBookingHolder> jwtTokenBookingHolder = new ArrayList<JwtTokenBookingHolder>();
		try {
			jwtTokenBookingHolder.addAll(Optional.ofNullable(Arrays.asList(loadJSON(repositoryPath.resolve(repositoryPath.toAbsolutePath()).toFile(), getRepositoryClass().getSimpleName() + ".json", JwtTokenBookingHolder[].class)))
					.orElse(new ArrayList<JwtTokenBookingHolder>()));
		} catch (Exception e) {
			throw new JwtException(e.getMessage(), "JWT_FIND_ALL_EXCEPTION");
		}

		return jwtTokenBookingHolder;
	}

	@Override
	public final JwtTokenBookingHolder save(
			JwtTokenBookingHolder entity) throws JwtException {
		entity.setJwtTokenBookingholderId(UUID.randomUUID().toString());
		List<JwtTokenBookingHolder> jwtTokebookingHolders = new ArrayList<JwtTokenBookingHolder>();
		jwtTokebookingHolders.addAll(findAll());
		jwtTokebookingHolders.add(entity);
		write(jwtTokebookingHolders);
		return entity;
	}

	public static void main(
			String[] args) {
		try {
			JwtTokenBookingHolderRepository repository = new JwtTokenBookingHolderRepository();
			JwtTokenBookingHolder holder = new JwtTokenBookingHolder();
			holder.setUsername("tuiflyuser");
			holder.setPassword(new BCryptPasswordEncoder(12).encode("tuiflypassword"));

			holder.setEmail("mario.rossi@rossi.it");
			holder.setRoles(JwtTokenBookingHolderRole.values());

			repository.save(holder);
			repository.findByUsername("tuiflyuser");
		} catch (JwtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
