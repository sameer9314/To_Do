package com.bridgelabz.todoapplication.sequence.dao;

import com.bridgelabz.todoapplication.sequence.exception.SequenceException;

public interface SequenceDao {

	String getNextSequenceId(String key) throws SequenceException;

}