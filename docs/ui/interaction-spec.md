# Arena Interaction, Motion & Responsive Specification

## 1. Motion Choreography
* **Shimmer Loaders**: 1200ms `FastOutSlowInEasing` continuous gradient sweep to eliminate layout shifts (CLS < 0.05).
* **Live Match Pulse**: 800ms heartbeat breathing scale (`1.0x -> 1.05x`) for live games.
* **Modal Bottom Sheets**: Spring-assisted slide-in from bottom with tactile dismiss gesture.

## 2. Responsive Breakpoint Rules
* **Compact Phone (< 600dp)**: Persistent 5-tab Bottom Navigation bar.
* **Foldable & Tablet (>= 600dp)**: 2-column dashboard layout with expanded bracket canvas.

## 3. Real State Machine (Join Tournament Flow)
```
[Unregistered] -> Tap 'Join Tournament' -> Validation Check -> Slot Decremented -> [Joined / Registered]
                                                                                        |
                                                                                        v
                                                                   Displays 'Your Match Room Card'
                                                                   (Room ID, Password, Map, Slot #)
```
