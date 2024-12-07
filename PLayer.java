package com.chess.engine.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.attackMove;
import com.chess.engine.pieces.Piece;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean IsInCheck;
    
    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves) {
    
        this.board = board;
        this.playerKing = establishKing();
        this. legalMoves = legalMoves;
        this.IsInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    } 

    public King getPlayerKing(){
        return this.playerKing;
    }

    public Collection<Move> getlegalMoves(){
        return this.legalMoves;
    }

    private King establishKing() {
        for(final Piece piece : getActivePieces()) {
            if(piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Should not reach here! Not a valid board!!");
        }
    public abstract Collection<Piece> getActivePieces();


    private static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList();
        for(final Move move : moves) {
            if(piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
                }
            }

        return ImmutableList.copy0f(attackMoves);
    }





    public boolean IsMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }
    public boolean IsInCheck(){
        return this.IsInCheck;
    }
    public boolean IsInCheckMate(){
        return this.IsInCheck && !hasEscapeMoves();
    }
    public boolean IsInstalekMate(){
        return this.IsInCheck && !hasEscapeMoves();
        }

    protected boolean hasEscapeMoves(){
        for(final Move move : this.legalMoves){
            final MoveTransition transition = makeMobe(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }

    public boolean IsCastled(){
        return false;
    }
    public MoveTransition makeMobe(final Move move){
        if(!IsMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getopponent().getPlayerKing().getPiecePosition(),
transitionBoard.currentPlayer().getlegalMoves());
    if(!kingAttacks.isEmpty()) {
        return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
    }
    return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Alliance getAlliance();
    public abstract Player getopponent();
}
