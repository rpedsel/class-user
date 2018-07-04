package classusers;

public class View {

	interface General {}
	interface Student extends General {}
	interface User extends General {}
	interface Class extends Student {}
}