package dev.kavrin.spring_boot_crash_course

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Disabled
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Disabled("Disabled pending dedicated test configuration (Mongo & JWT setup)")
class SpringBootCrashCourseApplicationTests {

	@Test
	fun contextLoads() {
	}

}
