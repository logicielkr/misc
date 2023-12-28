SENDER="보내는 사람 이메일 주소"
RECIPIENT="받는 사람 이메일 주소"
subject="제목"
message="내용"

sendmail -v -f ${SENDER} ${RECIPIENT} <<EOF
From: <${SENDER}>
To: <${RECIPIENT}>
Subject: ${subject}

${message}

EOF
