SENDER="보내는 사람 이메일 주소"
RECIPIENT="받는 사람 이메일 주소"
subject="제목"
message="내용"

mailx -r "${SENDER}" -s "${subject}" "${RECIPIENT}" <<EOF
${message}
EOF
