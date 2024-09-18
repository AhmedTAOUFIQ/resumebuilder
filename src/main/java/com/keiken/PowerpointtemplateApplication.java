package com.keiken;

import com.keiken.config.AppProperties;
import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.service.TemplateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(AppProperties.class)
public class PowerpointtemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(PowerpointtemplateApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(TemplateService templateService) {
		return (args) -> {


			// Create sample data for TemplateData
			TemplateBaseMapper templateData = TemplateBaseMapper.builder()
					.name("Hamza Salmane")
					.city("Casablanca")
					.abstractProfile("Experienced Software Developer with a strong background in Java SE/Java EE and Spring Boot, specializing in building scalable and efficient microservices architectures. Adept at full-stack development with a proven track record of delivering high-quality web applications using technologies such as React, Angular, and PHP (Laravel). Proficient in modern DevOps practices including Docker, CI/CD with Jenkins, and cloud services like AWS")
					.cvRole("Software Engineer")
					.facts(List.of(
							new TemplateBaseMapper.Fact(
									"Software Engineer",
									"2020",
									"2023",
									TemplateBaseMapper.FactType.experience,
									"Developed various applications using Java.\n" +
											"Developed various applications using Docker.\n" +
											"Developed various applications using kubernetes.\n" +
											"Developed various applications using Python.\n"
											,
									"capgemini",
									"City A",
									List.of(
											new TemplateBaseMapper.Skill("1", "Java"),
											new TemplateBaseMapper.Skill("2", "Spring Boot"),
											new TemplateBaseMapper.Skill("3", "Docker"),
											new TemplateBaseMapper.Skill("4", "Kubernetes")
									)
							),
							new TemplateBaseMapper.Fact(
									"Software Engineer",
									"2020",
									"2023",
									TemplateBaseMapper.FactType.experience,
									"Developed various applications using Laravel.",
									"capgemini",
									"City A",
									List.of(
											new TemplateBaseMapper.Skill("1", "Java"),
											new TemplateBaseMapper.Skill("2", "Spring Boot")
									)
							),
							new TemplateBaseMapper.Fact(
									"Software Engineer",
									"2020",
									"2023",
									TemplateBaseMapper.FactType.experience,
									"Developed various applications using React.",
									"capgemini",
									"City A",
									List.of(
											new TemplateBaseMapper.Skill("1", "Java"),
											new TemplateBaseMapper.Skill("2", "Spring Boot")
									)
							),
							new TemplateBaseMapper.Fact(
									"Software Engineer",
									"2020",
									"2017",
									TemplateBaseMapper.FactType.experience,
									"Developed various applications using Angular.",
									"Amzon",
									"City A",
									List.of(
											new TemplateBaseMapper.Skill("1", "Java"),
											new TemplateBaseMapper.Skill("2", "Spring Boot")
									)
							),
							new TemplateBaseMapper.Fact(
									"Software Engineer",
									"2020",
									"2023",
									TemplateBaseMapper.FactType.experience,
									"Developed various applications using Docker.",
									"Uber",
									"City A",
									List.of(
											new TemplateBaseMapper.Skill("1", "Java"),
											new TemplateBaseMapper.Skill("2", "Spring Boot")
									)
							),
							new TemplateBaseMapper.Fact(
									"Software Engineer",
									"2020",
									"2023",
									TemplateBaseMapper.FactType.experience,
									"Developed various applications using Vue js.",
									"Adobe",
									"City A",
									List.of(
											new TemplateBaseMapper.Skill("1", "Java"),
											new TemplateBaseMapper.Skill("2", "Spring Boot")
									)
							),
							new TemplateBaseMapper.Fact(
									"Software Engineer",
									"2020",
									"2023",
									TemplateBaseMapper.FactType.experience,
									"Developed various applications using Java.\n" +
											"Developed various applications using Docker.\n" +
											"Developed various applications using kubernetes.\n" +
											"Developed various applications using Python.\n" +
											"Developed various applications using Vue js.\n" +
											"Developed various applications using React js",
									"Alan",
									"City A",
									List.of(
											new TemplateBaseMapper.Skill("1", "Java"),
											new TemplateBaseMapper.Skill("2", "Spring Boot")
									)
							),
							new TemplateBaseMapper.Fact(
									"Project Manager",
									"2018",
									"2020",
									TemplateBaseMapper.FactType.experience,
									"Managed multiple projects and teams.",
									"Alten",
									"City B",
									List.of(
											new TemplateBaseMapper.Skill("1", "Java"),
											new TemplateBaseMapper.Skill("3", "Angular"),
											new TemplateBaseMapper.Skill("3", "CICD"),
											new TemplateBaseMapper.Skill("3", "AWS")
									)
							),

							new TemplateBaseMapper.Fact(
									"azure_fundamentals",
									"2022",
									"2022",
									TemplateBaseMapper.FactType.certification,
									"Completed certification in Advanced Java.",
									"Institution C",
									"City C",
									List.of(
											new TemplateBaseMapper.Skill("1", "Python"),
											new TemplateBaseMapper.Skill("3", "Docker"),
											new TemplateBaseMapper.Skill("3", "Flask")
									)
							),
							new TemplateBaseMapper.Fact(
									"ccna_certified",
									"2022",
									"2022",
									TemplateBaseMapper.FactType.certification,
									"Completed certification in Advanced Java.",
									"Institution C",
									"City C",
									List.of(
											new TemplateBaseMapper.Skill("1", "Python"),
											new TemplateBaseMapper.Skill("3", "Docker"),
											new TemplateBaseMapper.Skill("3", "Flask")
									)
							),
							new TemplateBaseMapper.Fact(
									"hcia_huawei",
									"2022",
									"2022",
									TemplateBaseMapper.FactType.certification,
									"Completed certification in Advanced Java.",
									"Institution C",
									"City C",
									List.of(
											new TemplateBaseMapper.Skill("1", "Python"),
											new TemplateBaseMapper.Skill("3", "Docker"),
											new TemplateBaseMapper.Skill("3", "Flask")
									)
							),
							new TemplateBaseMapper.Fact(
									"java_certified_programmer",
									"2022",
									"2022",
									TemplateBaseMapper.FactType.certification,
									"Completed certification in Advanced Java.",
									"Institution C",
									"City C",
									List.of(
											new TemplateBaseMapper.Skill("1", "Python"),
											new TemplateBaseMapper.Skill("2", "Docker"),
											new TemplateBaseMapper.Skill("3", "Flask"),
											new TemplateBaseMapper.Skill("4", "cobol"),
											new TemplateBaseMapper.Skill("5", "zig")

									)
							),
							new TemplateBaseMapper.Fact(
									"kubernetes_appdev",
									"2016",
									"2020",
									TemplateBaseMapper.FactType.certification,
									"Bachelor's degree from University ENSAH.",
									"University ENSAH",
									"Hoceima ",
									new ArrayList<>()
							),
							new TemplateBaseMapper.Fact(
									"ensate",
									"2010",
									"2020",
									TemplateBaseMapper.FactType.education,
									"Bachelor's degree from University ensate with science physique.",
									"University ensate ",
									"Errachidia",
									new ArrayList<>()
							),
							new TemplateBaseMapper.Fact(
									"ensam",
									"2010",
									"2018",
									TemplateBaseMapper.FactType.education,
									"Bachelor's degree from University ensam with science physique.",
									"ENSAM ",
									"Errachidia",
									new ArrayList<>()
							)
					))
					.build();

					templateService.processTemplate("keiken.html", templateData);
		};
	}

}
