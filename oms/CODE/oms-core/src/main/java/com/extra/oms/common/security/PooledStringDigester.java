package com.extra.oms.common.security;

import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PooledStringDigester {

	private final StringDigester firstDigester;

	@Value("${security.encrypt.poolsize: 100}")
	private int poolSize = 0;

	private StringDigester[] pool;

	private int roundRobin = 0;

	public PooledStringDigester() {
		super();
		this.firstDigester = new StringDigester();
	}

	@PostConstruct
	public synchronized void initialize() throws NoSuchAlgorithmException {		

		if (this.poolSize <= 0) {
			throw new IllegalArgumentException("Pool size must be set and > 0");
		}
		firstDigester.initialize();
		this.pool = new StringDigester[this.poolSize];
		this.pool[0] = this.firstDigester;
		for (int i = 1; i < this.poolSize; i++) {
			this.pool[i] = this.pool[i - 1].cloneDigester();
		}
	}

	public String digest(final String message) {

        int poolPosition;
        synchronized(this) {
            poolPosition = this.roundRobin;
            this.roundRobin = (this.roundRobin + 1) % this.poolSize;
        }
        return this.pool[poolPosition].digest(message);
    }

	public boolean matches(final String message, final String digest) {

        int poolPosition;
        synchronized(this) {
            poolPosition = this.roundRobin;
            this.roundRobin = (this.roundRobin + 1) % this.poolSize;
        }
        return this.pool[poolPosition].matches(message, digest);
    }

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
}
