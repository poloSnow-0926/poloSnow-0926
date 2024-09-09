package net;

/**
 * 在请求中携带的用户信息
 */

public class Session {

	private Student student = null;
	private Teacher teacher = null;
	private UserType userType = null;

	public Student getStudent() {
		return student;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public UserType getUserType() {
		return userType;
	}

	public Session() {}
	
	public Session(Student student) {
		this.student = student;
		this.userType = UserType.STUDENT;
	}

	public Session(Teacher teacher) {
		this.teacher = teacher;
		this.userType = UserType.TEACHER;
	}

	@Override
	public String toString() {
		return "Session [student=" + student + ", teacher=" + teacher + ", userType=" + userType
				+ "]";
	}

}