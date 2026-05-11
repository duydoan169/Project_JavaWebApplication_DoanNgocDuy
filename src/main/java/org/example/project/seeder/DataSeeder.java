package org.example.project.seeder;

import lombok.RequiredArgsConstructor;
import org.example.project.model.enums.Gender;
import org.example.project.model.enums.Role;
import org.example.project.model.*;
import org.example.project.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SpecialtyRepository specialtyRepository;
    private final TestTypeRepository testTypeRepository;
    private final DoctorRepository doctorRepository;
    private final MedicineRepository medicineRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedSpecialties();
        seedTestTypes();
        seedMedicines();
        seedAdminUser();
        seedDoctors();
    }

    private void seedSpecialties() {
        if (specialtyRepository.count() > 0) return;

        List<String> names = List.of(
                "Nội khoa", "Ngoại khoa", "Nhi khoa", "Sản - Phụ khoa",
                "Da liễu", "Tim mạch", "Thần kinh", "Mắt", "Tai Mũi Họng", "Răng Hàm Mặt"
        );

        for (String name : names) {
            Specialty s = new Specialty();
            s.setName(name);
            specialtyRepository.save(s);
        }
    }

    private void seedTestTypes() {
        if (testTypeRepository.count() > 0) return;

        TestType t1 = new TestType();
        t1.setName("Xét nghiệm máu toàn phần (CBC)");
        t1.setUnit("cells/μL");
        testTypeRepository.save(t1);

        TestType t2 = new TestType();
        t2.setName("Glucose máu lúc đói");
        t2.setUnit("mg/dL");
        testTypeRepository.save(t2);

        TestType t3 = new TestType();
        t3.setName("HbA1c");
        t3.setUnit("%");
        testTypeRepository.save(t3);

        TestType t4 = new TestType();
        t4.setName("Lipid máu");
        t4.setUnit("mg/dL");
        testTypeRepository.save(t4);

        TestType t5 = new TestType();
        t5.setName("ALT / AST");
        t5.setUnit("U/L");
        testTypeRepository.save(t5);

        TestType t6 = new TestType();
        t6.setName("Tổng phân tích nước tiểu");
        t6.setUnit("N/A");
        testTypeRepository.save(t6);

        TestType t7 = new TestType();
        t7.setName("X-Quang ngực thẳng");
        t7.setUnit("N/A");
        testTypeRepository.save(t7);

        TestType t8 = new TestType();
        t8.setName("Siêu âm ổ bụng");
        t8.setUnit("N/A");
        testTypeRepository.save(t8);

        TestType t9 = new TestType();
        t9.setName("Điện tâm đồ (ECG)");
        t9.setUnit("N/A");
        testTypeRepository.save(t9);
    }

    private void seedMedicines() {
        if (medicineRepository.count() > 0) return;

        Medicine m1 = new Medicine();
        m1.setName("Paracetamol 500mg");
        m1.setUnit("Viên");
        m1.setStockQuantity(500);
        m1.setPricePerUnit(new BigDecimal("500.00"));
        medicineRepository.save(m1);

        Medicine m2 = new Medicine();
        m2.setName("Amoxicillin 500mg");
        m2.setUnit("Viên");
        m2.setStockQuantity(300);
        m2.setPricePerUnit(new BigDecimal("1500.00"));
        medicineRepository.save(m2);

        Medicine m3 = new Medicine();
        m3.setName("Ibuprofen 400mg");
        m3.setUnit("Viên");
        m3.setStockQuantity(200);
        m3.setPricePerUnit(new BigDecimal("800.00"));
        medicineRepository.save(m3);

        Medicine m4 = new Medicine();
        m4.setName("Omeprazole 20mg");
        m4.setUnit("Viên");
        m4.setStockQuantity(150);
        m4.setPricePerUnit(new BigDecimal("2000.00"));
        medicineRepository.save(m4);

        Medicine m5 = new Medicine();
        m5.setName("Metformin 500mg");
        m5.setUnit("Viên");
        m5.setStockQuantity(400);
        m5.setPricePerUnit(new BigDecimal("700.00"));
        medicineRepository.save(m5);

        Medicine m6 = new Medicine();
        m6.setName("Atorvastatin 20mg");
        m6.setUnit("Viên");
        m6.setStockQuantity(250);
        m6.setPricePerUnit(new BigDecimal("3500.00"));
        medicineRepository.save(m6);

        Medicine m7 = new Medicine();
        m7.setName("Salbutamol 100mcg");
        m7.setUnit("Lọ");
        m7.setStockQuantity(80);
        m7.setPricePerUnit(new BigDecimal("45000.00"));
        medicineRepository.save(m7);

        Medicine m8 = new Medicine();
        m8.setName("ORS bù điện giải");
        m8.setUnit("Gói");
        m8.setStockQuantity(600);
        m8.setPricePerUnit(new BigDecimal("5000.00"));
        medicineRepository.save(m8);
    }

    private void seedAdminUser() {
        if (userRepository.existsUserByUsername("admin")) return;

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        admin.setFullName("Quản Trị Viên");
        admin.setPhone("0900000000");
        admin.setCreatedAt(LocalDateTime.now());
        userRepository.save(admin);
    }

    private void seedDoctors() {
        if (doctorRepository.count() > 0) return;

        Specialty noiKhoa = specialtyRepository.findByName("Nội khoa").orElseThrow();
        Specialty timMach = specialtyRepository.findByName("Tim mạch").orElseThrow();
        Specialty nhiKhoa = specialtyRepository.findByName("Nhi khoa").orElseThrow();

        User u1 = new User();
        u1.setUsername("doctor1");
        u1.setEmail("doctor1@healthcare.vn");
        u1.setPassword(passwordEncoder.encode("Doctor@123"));
        u1.setRole(Role.DOCTOR);
        u1.setFullName("Nguyễn Văn An");
        u1.setPhone("0911000001");
        u1.setGender(Gender.MALE);
        u1.setCreatedAt(LocalDateTime.now());
        userRepository.save(u1);

        Doctor d1 = new Doctor();
        d1.setUser(u1);
        d1.setSpecialty(noiKhoa);
        doctorRepository.save(d1);

        User u2 = new User();
        u2.setUsername("doctor2");
        u2.setEmail("doctor2@healthcare.vn");
        u2.setPassword(passwordEncoder.encode("Doctor@123"));
        u2.setRole(Role.DOCTOR);
        u2.setFullName("Trần Thị Bình");
        u2.setPhone("0911000002");
        u2.setGender(Gender.FEMALE);
        u2.setCreatedAt(LocalDateTime.now());
        userRepository.save(u2);

        Doctor d2 = new Doctor();
        d2.setUser(u2);
        d2.setSpecialty(timMach);
        doctorRepository.save(d2);

        User u3 = new User();
        u3.setUsername("doctor3");
        u3.setEmail("doctor3@healthcare.vn");
        u3.setPassword(passwordEncoder.encode("Doctor@123"));
        u3.setRole(Role.DOCTOR);
        u3.setFullName("Lê Minh Châu");
        u3.setPhone("0911000003");
        u3.setGender(Gender.MALE);
        u3.setCreatedAt(LocalDateTime.now());
        userRepository.save(u3);

        Doctor d3 = new Doctor();
        d3.setUser(u3);
        d3.setSpecialty(nhiKhoa);
        doctorRepository.save(d3);
    }
}