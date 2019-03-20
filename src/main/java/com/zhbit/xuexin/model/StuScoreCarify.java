package com.zhbit.xuexin.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "V_STUSCORECARIFY")
public class StuScoreCarify {
		private String id;
		private String studentno;
		private String stuname;
		private String grade;
		private String majorcode;
		private String major;
		private String academicyear;
		private String term;
		private String classname;
		private String teacherno;
		private String teachername;
		private String coursecode;
		private String courseName;
		private String retakeflag;
		private String usualscore;
		private String middlescore;
		private String endscore;
		private String labscore;
		private String finalscore;
		private String convertscore;
		private String resitscore;
		private String repairscore;
		private String coursetype;
		private String selectedcourseno;

		private String tusualscore;
		private String tmiddlescore;
		private String tendscore;
		private String tlabscore;
		private String tfinalscore;
		private String tconvertscore;
		private String tresitscore;
		private String trepairscore;
		
		private String memo;
		private String resitmemo;
		
		public StuScoreCarify(){}
		
		public StuScoreCarify(String id, String studentno, String stuname, String grade, String majorcode,
                              String major, String academicyear, String term, String classname, String teacherno, String teachername,
                              String coursecode, String courseName, String retakeflag, String usualscore, String middlescore,
                              String endscore, String labscore, String finalscore, String convertscore, String resitscore,
                              String repairscore, String coursetype, String selectedcourseno, String tusualscore, String tmiddlescore,
                              String tendscore, String tlabscore, String tfinalscore, String tconvertscore, String tresitscore,
                              String trepairscore, String memo, String resitmemo) {
			super();
			this.id = id;
			this.studentno = studentno;
			this.stuname = stuname;
			this.grade = grade;
			this.majorcode = majorcode;
			this.major = major;
			this.academicyear = academicyear;
			this.term = term;
			this.classname = classname;
			this.teacherno = teacherno;
			this.teachername = teachername;
			this.coursecode = coursecode;
			this.courseName = courseName;
			this.retakeflag = retakeflag;
			this.usualscore = usualscore;
			this.middlescore = middlescore;
			this.endscore = endscore;
			this.labscore = labscore;
			this.finalscore = finalscore;
			this.convertscore = convertscore;
			this.resitscore = resitscore;
			this.repairscore = repairscore;
			this.coursetype = coursetype;
			this.selectedcourseno = selectedcourseno;
			this.tusualscore = tusualscore;
			this.tmiddlescore = tmiddlescore;
			this.tendscore = tendscore;
			this.tlabscore = tlabscore;
			this.tfinalscore = tfinalscore;
			this.tconvertscore = tconvertscore;
			this.tresitscore = tresitscore;
			this.trepairscore = trepairscore;
			this.memo = memo;
			this.resitmemo = resitmemo;
		}

		@GenericGenerator(name = "generator", strategy = "uuid.hex")
		@Id
		@GeneratedValue(generator = "generator")
		@Column(name = "ID", unique = true, nullable = false)
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getStudentno() {
			return studentno;
		}
		
		@Column(name = "STUDENTNO")
		public void setStudentno(String studentno) {
			this.studentno = studentno;
		}
		 
		public String getStuname() {
			return stuname;
		}
		@Column(name = "STUNAME")
		public void setStuname(String stuname) {
			this.stuname = stuname;
		}

		public String getGrade() {
			return grade;
		}
		@Column(name = "GRADE")
		public void setGrade(String grade) {
			this.grade = grade;
		}
		public String getMajorcode() {
			return majorcode;
		}
		@Column(name = "MAJORCODE")
		public void setMajorcode(String majorcode) {
			this.majorcode = majorcode;
		}
		public String getMajor() {
			return major;
		}
		@Column(name = "MAJOR")
		public void setMajor(String major) {
			this.major = major;
		}
		public String getAcademicyear() {
			return academicyear;
		}
		@Column(name = "ACADEMICYEAR")
		public void setAcademicyear(String academicyear) {
			this.academicyear = academicyear;
		}
		public String getTerm() {
			return term;
		}
		@Column(name = "TERM")
		public void setTerm(String term) {
			this.term = term;
		}
		public String getClassname() {
			return classname;
		}
		@Column(name = "CLASSNAME")
		public void setClassname(String classname) {
			this.classname = classname;
		}
		public String getTeacherno() {
			return teacherno;
		}
		@Column(name = "TEACHERNO")
		public void setTeacherno(String teacherno) {
			this.teacherno = teacherno;
		}
		public String getTeachername() {
			return teachername;
		}
		@Column(name = "TEACHERNAME")
		public void setTeachername(String teachername) {
			this.teachername = teachername;
		}
		public String getCoursecode() {
			return coursecode;
		}
		@Column(name = "COURSECODE")
		public void setCoursecode(String coursecode) {
			this.coursecode = coursecode;
		}
		public String getCourseName() {
			return courseName;
		}
		@Column(name = "COURSENAME")
		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}
		public String getRetakeflag() {
			return retakeflag;
		}
		@Column(name = "RETAKEFLAG")
		public void setRetakeflag(String retakeflag) {
			this.retakeflag = retakeflag;
		}
		public String getUsualscore() {
			return usualscore;
		}
		@Column(name = "USUALSCORE")
		public void setUsualscore(String usualscore) {
			this.usualscore = usualscore;
		}
		public String getMiddlescore() {
			return middlescore;
		}
		@Column(name = "MIDDLESCORE")
		public void setMiddlescore(String middlescore) {
			this.middlescore = middlescore;
		}
		public String getEndscore() {
			return endscore;
		}
		@Column(name = "ENDSCORE")
		public void setEndscore(String endscore) {
			this.endscore = endscore;
		}
		
		public String getLabscore() {
			return labscore;
		}
		@Column(name = "LABSCORE")
		public void setLabscore(String labscore) {
			this.labscore = labscore;
		}

		public String getFinalscore() {
			return finalscore;
		}
		@Column(name = "FINALSCORE")
		public void setFinalscore(String finalscore) {
			this.finalscore = finalscore;
		}
		public String getConvertscore() {
			return convertscore;
		}
		@Column(name = "CONVERTSCORE")
		public void setConvertscore(String convertscore) {
			this.convertscore = convertscore;
		}
		public String getResitscore() {
			return resitscore;
		}
		@Column(name = "RESITSCORE")
		public void setResitscore(String resitscore) {
			this.resitscore = resitscore;
		}
		public String getRepairscore() {
			return repairscore;
		}
		@Column(name = "REPAIRSCORE")
		public void setRepairscore(String repairscore) {
			this.repairscore = repairscore;
		}
		public String getCoursetype() {
			return coursetype;
		}
		@Column(name = "COURSETYPE")
		public void setCoursetype(String coursetype) {
			this.coursetype = coursetype;
		}
		public String getSelectedcourseno() {
			return selectedcourseno;
		}
		@Column(name = "SELECTEDCOURSENO")
		public void setSelectedcourseno(String selectedcourseno) {
			this.selectedcourseno = selectedcourseno;
		}
 
		public String getMemo() {
			return memo;
		}

		@Column(name = "MEMO")
		public void setMemo(String memo) {
			this.memo = memo;
		}


		public String getResitmemo() {
			return resitmemo;
		}

		@Column(name = "RESIMEMO")
		public void setResitmemo(String resitmemo) {
			this.resitmemo = resitmemo;
		}

		public String getTusualscore() {
			return tusualscore;
		}
		@Column(name = "TUSUALSCORE")
		public void setTusualscore(String tusualscore) {
			this.tusualscore = tusualscore;
		}
		
		public String getTmiddlescore() {
			return tmiddlescore;
		}
		@Column(name = "TMIDDLESCORE")
		public void setTmiddlescore(String tmiddlescore) {
			this.tmiddlescore = tmiddlescore;
		}

		public String getTendscore() {
			return tendscore;
		}
		@Column(name = "TENDSCORE")
		public void setTendscore(String tendscore) {
			this.tendscore = tendscore;
		}

		public String getTlabscore() {
			return tlabscore;
		}
		@Column(name = "TLABSCORE")
		public void setTlabscore(String tlabscore) {
			this.tlabscore = tlabscore;
		}

		public String getTfinalscore() {
			return tfinalscore;
		}
		@Column(name = "TFINALSCORE")
		public void setTfinalscore(String tfinalscore) {
			this.tfinalscore = tfinalscore;
		}

		public String getTconvertscore() {
			return tconvertscore;
		}
		@Column(name = "TCONVERTSCORE")
		public void setTconvertscore(String tconvertscore) {
			this.tconvertscore = tconvertscore;
		}

		public String getTresitscore() {
			return tresitscore;
		}
		@Column(name = "TRESITSCORE")
		public void setTresitscore(String tresitscore) {
			this.tresitscore = tresitscore;
		}

		public String getTrepairscore() {
			return trepairscore;
		}
		@Column(name = "TREPAIRSCORE")
		public void setTrepairscore(String trepairscore) {
			this.trepairscore = trepairscore;
		}
		
}
