# Feature: Chat

## OVERVIEW

채팅 기능 모듈. 채팅방 목록(Chat)과 개별 대화(ChatRoom) 두 화면으로 구성.
예약 상태별 필터링, 다양한 메시지 유형(텍스트, 이미지, 알림, 거래확정 등) 지원.

## STRUCTURE

```
feature/chat/src/main/kotlin/com/hm/picplz/ui/screen/
├── chat/                    # 채팅방 목록 (List)
│   ├── ChatScreen.kt        # 탭(진행중/완료됨) + 상태 필터
│   ├── ChatViewModel.kt
│   ├── ChatState.kt         # ChatTabType, filteredChatRooms
│   ├── ChatIntent.kt
│   ├── ChatSideEffect.kt
│   └── composable/
│       ├── ChatRoomList.kt
│       ├── ChatRoomListItem.kt
│       └── ChatStatusTag.kt
│
└── chat_room/               # 개별 채팅방 (Detail)
    ├── ChatRoomScreen.kt    # 메시지 리스트 + 입력창
    ├── ChatRoomViewModel.kt
    ├── ChatRoomState.kt     # ReservationStep, ChatListItem
    ├── ChatRoomIntent.kt
    ├── ChatRoomSideEffect.kt
    └── composable/
        ├── ChatInput.kt         # 메시지 입력 컴포넌트
        ├── ChatTextField.kt
        ├── ChatMessageProfile.kt
        ├── ReservationStep.kt   # 예약 진행 단계 표시
        ├── AlarmSwipe.kt
        └── bubble/              # 메시지 버블 컴포넌트
            ├── ChatBubbleSurface.kt   # 공통 버블 Surface
            ├── ChatMessageBubble.kt   # 텍스트 메시지
            ├── ImageChat.kt           # 이미지 메시지
            ├── NotificationBubble.kt  # 알림 버블
            ├── DealConfirmationBubble.kt
            ├── CompleteBubble.kt
            ├── ChangeTimeBubble.kt
            ├── ChatSuggest.kt
            └── ChatSuggestButton.kt
```

## COMPONENTS

| Component | Description |
|-----------|-------------|
| `ChatBubbleSurface` | 방향(SENT/RECEIVED)에 따른 공통 버블 스타일 |
| `ChatMessageBubble` | 텍스트 메시지 버블 (최대 너비 제한) |
| `ImageChat` | 이미지 메시지 (복수 이미지 지원) |
| `NotificationBubble` | 시스템 알림 (POSITIVE/NEGATIVE 타입) |
| `DealConfirmationBubble` | 거래 확정 요청 버블 |
| `ChatInput` | 메뉴 버튼 + 텍스트 입력 + 전송 버튼 |
| `ChatStatusTag` | 상태 필터 태그 (예약대기, 확정, 거절, 완료) |
